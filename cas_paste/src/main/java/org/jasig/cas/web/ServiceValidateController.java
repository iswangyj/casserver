package org.jasig.cas.web;

import com.ume.TestUtil;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HttpBasedServiceCredential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.services.UnauthorizedProxyingException;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketValidationException;
import org.jasig.cas.ticket.UmeTicketValidationException;
import org.jasig.cas.ticket.proxy.ProxyHandler;
import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.Cas20ProtocolValidationSpecification;
import org.jasig.cas.validation.ValidationSpecification;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URL;

/**
 * @author SxL
 * Created on 9/25/2018 5:22 PM.
 */
public class ServiceValidateController extends DelegateController {
    public static final String DEFAULT_SERVICE_FAILURE_VIEW_NAME = "cas2ServiceFailureView";
    public static final String DEFAULT_SERVICE_SUCCESS_VIEW_NAME = "cas2ServiceSuccessView";
    private static final String MODEL_PROXY_GRANTING_TICKET_IOU = "pgtIou";
    private static final String MODEL_ASSERTION = "assertion";
    private static final String PARAMETER_PROXY_CALLBACK_URL = "pgtUrl";
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;
    @NotNull
    private Class<?> validationSpecificationClass = Cas20ProtocolValidationSpecification.class;
    @NotNull
    private ProxyHandler proxyHandler;
    @NotNull
    private String successView = "cas2ServiceSuccessView";
    @NotNull
    private String failureView = "cas2ServiceFailureView";
    @NotNull
    private ArgumentExtractor argumentExtractor;
    private UmeTicketValidationException tve = null;
    private String sql = "select a.account,a.user_sn,a.name,c.sid,c.appname,b.zhzt from spp_user a join cas_sys_user_apps b on a.account_id=b.user_unique_sid join cas_sys_apps c on b.app_sid=c.sid where a.account=? and (c.address_http like ? or c.address_https like ?)";
    private String sqlUserInfo = "select a.account,a.user_sn,a.name,a.account_id from spp_user a where account=?";
    private String sqlUserInfo_casassis = "select sid from cas_sys_user_unique where username_unique=?";
    private String sqlAppInfo_casassis = "select sid,appname from cas_sys_apps where address_http like ? or address_https like ?";
    private String sqlAppInfo = "select sid,appname from cas_sys_apps where cluster_nodes like ?";
    private String sqlAuthInfo = "select zhzt from cas_sys_user_apps where user_unique_sid=? and app_sid=?";
    private String sqlAuthInfo_casassis = "select 1 exist from cas_sys_user_apps where user_unique_sid=? and app_sid=?";
    private String sqlInsertLog = "insert into CAS_LOG_LOGIN(sid,union_account,user_sn,name,app_sid,app_name,login_time,success_falg,failure_reason,ip,resource) values(sys_guid(),?,?,?,?,?,sysdate,?,?,?,?)";
    @NotNull
    private JdbcTemplate jdbcTemplate;
    @NotNull
    private DataSource dataSource;

    public ServiceValidateController() {
    }

    protected Credential getServiceCredentialsFromRequest(HttpServletRequest request) {
        String pgtUrl = request.getParameter("pgtUrl");
        if (StringUtils.hasText(pgtUrl)) {
            try {
                return new HttpBasedServiceCredential(new URL(pgtUrl));
            } catch (Exception var4) {
                this.logger.error("Error constructing pgtUrl", var4);
            }
        }

        return null;
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.setRequiredFields(new String[]{"renew"});
    }

    @Override
    protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebApplicationService service = this.argumentExtractor.extractService(request);
        String serviceTicketId = service != null ? service.getArtifactId() : null;
        if (service != null && serviceTicketId != null) {
            try {
                Credential serviceCredential = this.getServiceCredentialsFromRequest(request);
                String proxyGrantingTicketId = null;
                if (serviceCredential != null) {
                    try {
                        proxyGrantingTicketId = this.centralAuthenticationService.delegateTicketGrantingTicket(serviceTicketId, new Credential[]{serviceCredential});
                    } catch (AuthenticationException var11) {
                        this.logger.info("Failed to authenticate service credential {}", serviceCredential);
                    } catch (TicketException var12) {
                        this.logger.error("Failed to create proxy granting ticket for {}", serviceCredential, var12);
                    }

                    if (StringUtils.isEmpty(proxyGrantingTicketId)) {
                        return this.generateErrorView("INVALID_PROXY_CALLBACK", "INVALID_PROXY_CALLBACK", new Object[]{serviceCredential.getId()});
                    }
                }

                Assertion assertion = this.centralAuthenticationService.validateServiceTicket(serviceTicketId, service);
                TestUtil.showServerAssertionInfo(assertion, this);
                ValidationSpecification validationSpecification = this.getCommandClass();
                ServletRequestDataBinder binder = new ServletRequestDataBinder(validationSpecification, "validationSpecification");
                this.initBinder(request, binder);
                binder.bind(request);
                if (!validationSpecification.isSatisfiedBy(assertion)) {
                    this.logger.debug("Service ticket [{}] does not satisfy validation specification.", serviceTicketId);
                    return this.generateErrorView("INVALID_TICKET", "INVALID_TICKET_SPEC", (Object[])null);
                } else {
                    String proxyIou = null;
                    if (serviceCredential != null && proxyGrantingTicketId != null && this.proxyHandler.canHandle(serviceCredential)) {
                        proxyIou = this.proxyHandler.handle(serviceCredential, proxyGrantingTicketId);
                        if (StringUtils.isEmpty(proxyIou)) {
                            return this.generateErrorView("INVALID_PROXY_CALLBACK", "INVALID_PROXY_CALLBACK", new Object[]{serviceCredential.getId()});
                        }
                    }

                    TestUtil.printInfo("ST validate successfully!", this);
                    this.onSuccessfulValidation(serviceTicketId, assertion, request, response);
                    TestUtil.printInfo("Authority validate successfully!", this);
                    this.logger.debug("Successfully validated service ticket {} for service [{}]", serviceTicketId, service.getId());
                    return this.generateSuccessView(assertion, proxyIou);
                }
            } catch (UmeTicketValidationException var13) {
                return this.generateErrorView(var13.getCode(), var13.getMessage(), new Object[]{serviceTicketId, var13.getOriginalService().getId(), service.getId()});
            } catch (TicketValidationException var14) {
                return this.generateErrorView(var14.getCode(), var14.getCode(), new Object[]{serviceTicketId, var14.getOriginalService().getId(), service.getId()});
            } catch (TicketException var15) {
                return this.generateErrorView(var15.getCode(), var15.getCode(), new Object[]{serviceTicketId});
            } catch (UnauthorizedProxyingException var16) {
                return this.generateErrorView(var16.getMessage(), var16.getMessage(), new Object[]{service.getId()});
            } catch (UnauthorizedServiceException var17) {
                return this.generateErrorView(var17.getMessage(), var17.getMessage(), (Object[])null);
            }
        } else {
            this.logger.debug("Could not identify service and/or service ticket. Service: {}, Service ticket id: {}", service, serviceTicketId);
            return this.generateErrorView("INVALID_REQUEST", "INVALID_REQUEST", (Object[])null);
        }
    }

    protected void onSuccessfulValidation(String serviceTicketId, Assertion assertion, HttpServletRequest request, HttpServletResponse response) throws UmeTicketValidationException {
        Service service = assertion.getService();
        String url = service.toString();
        String uid = assertion.getPrimaryAuthentication().getPrincipal().getId();

        if (!"orchange".equalsIgnoreCase(uid) && url.contains("admin")) {
            try {
                response.sendError(401);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        this.tve = null;
//        Service service = assertion.getService();
//        String url = service.toString();
//        String account_id = null;
//        String uid = null;
//        String user_sn = null;
//        String user_name = null;
//        String app_sid = null;
//        String app_name = null;
//        String errMsg = null;
//        String serv = CasServerCfgPub.getServicePath(url);
//        uid = assertion.getPrimaryAuthentication().getPrincipal().getId();
//        if (!"superman".equals(uid)) {
//            String ip = request.getRemoteAddr();
//            SqlRowSet rsUser;
//            SqlRowSet rsApp;
//            SqlRowSet rsZhzt;
//            String zhzt;
//            if (!CasServerCfgPub.getMode()) {
//                rsUser = this.jdbcTemplate.queryForRowSet(this.sqlUserInfo_casassis, new Object[]{uid});
//                rsUser.first();
//                if (null != rsUser && !rsUser.isAfterLast()) {
//                    account_id = rsUser.getString("sid");
//                    rsApp = this.jdbcTemplate.queryForRowSet(this.sqlAppInfo_casassis, new Object[]{serv + "%", serv + "%"});
//                    rsApp.first();
//                    if (null != rsApp && !rsApp.isAfterLast()) {
//                        try {
//                            app_sid = rsApp.getString("sid");
//                            app_name = rsApp.getString("appname");
//                        } catch (Exception var21) {
//                            errMsg = "authenticationFailure.UnManagedApplication";
//                            throw new UmeTicketValidationException("authenticationFailure.UnManagedApplication", errMsg, service);
//                        }
//
//                        rsZhzt = this.jdbcTemplate.queryForRowSet(this.sqlAuthInfo_casassis, new Object[]{account_id, app_sid});
//                        rsZhzt.first();
//                        if (null != rsZhzt && !rsZhzt.isAfterLast()) {
//                            zhzt = null;
//
//                            try {
//                                zhzt = rsZhzt.getString("exist");
//                            } catch (Exception var20) {
//                                var20.printStackTrace();
//                                errMsg = "authenticationFailure.CurrUserNoAccessCurrApp";
//                                throw new UmeTicketValidationException("authenticationFailure.CurrUserNoAccessCurrApp", errMsg, service);
//                            }
//
//                            if (null == zhzt || !"1".equals(zhzt)) {
//                                errMsg = "authenticationFailure.CurrUserNoAccessCurrApp";
//                                throw new UmeTicketValidationException("authenticationFailure.CurrUserNoAccessCurrApp", errMsg, service);
//                            }
//                        } else {
//                            errMsg = "authenticationFailure.CurrUserNoAccessCurrApp";
//                            throw new UmeTicketValidationException("authenticationFailure.CurrUserNoAccessCurrApp", errMsg, service);
//                        }
//                    } else {
//                        errMsg = "authenticationFailure.UnmanagedApplication";
//                        throw new UmeTicketValidationException("authenticationFailure.UnmanagedApplication", errMsg, service);
//                    }
//                } else {
//                    errMsg = "authenticationFailure.UnionAccountNotExist";
//                    throw new UmeTicketValidationException("authenticationFailure.UnionAccountNotExist", errMsg, service);
//                }
//            } else if (null == CasServerCfgPub.getPortalPage() || serv.indexOf(CasServerCfgPub.getPortalPage()) == -1) {
//                rsUser = this.jdbcTemplate.queryForRowSet(this.sqlUserInfo, new Object[]{uid});
//                rsUser.first();
//                if (null != rsUser && !rsUser.isAfterLast()) {
//                    account_id = rsUser.getString("account_id");
//                    user_sn = rsUser.getString("user_sn");
//                    user_name = rsUser.getString("name");
//                    rsApp = this.jdbcTemplate.queryForRowSet(this.sqlAppInfo, new Object[]{"%" + serv + "%"});
//                    rsApp.first();
//                    if (null != rsApp && !rsApp.isAfterLast()) {
//                        try {
//                            app_sid = rsApp.getString("sid");
//                            app_name = rsApp.getString("appname");
//                        } catch (Exception var23) {
//                            errMsg = "authenticationFailure.UnManagedApplication";
//                            this.jdbcTemplate.update(this.sqlInsertLog, new Object[]{uid, account_id, user_name, app_sid, serv, "2", errMsg, ip, url});
//                            throw new UmeTicketValidationException("authenticationFailure.UnManagedApplication", errMsg, service);
//                        }
//
//                        rsZhzt = this.jdbcTemplate.queryForRowSet(this.sqlAuthInfo, new Object[]{account_id, app_sid});
//                        rsZhzt.first();
//                        if (null != rsZhzt && !rsZhzt.isAfterLast()) {
//                            zhzt = null;
//
//                            try {
//                                zhzt = rsZhzt.getString("zhzt");
//                            } catch (Exception var22) {
//                                var22.printStackTrace();
//                                errMsg = "authenticationFailure.CurrUserNoAccessCurrApp";
//                                this.jdbcTemplate.update(this.sqlInsertLog, new Object[]{uid, account_id, user_name, app_sid, app_name, "2", errMsg, ip, url});
//                                throw new UmeTicketValidationException("authenticationFailure.CurrUserNoAccessCurrApp", errMsg, service);
//                            }
//
//                            if (null != zhzt && !"".equals(zhzt) && !"2".equals(zhzt)) {
//                                this.jdbcTemplate.update(this.sqlInsertLog, new Object[]{uid, account_id, user_name, app_sid, app_name, "1", null, ip, url});
//                            } else {
//                                errMsg = "authenticationFailure.CurrUserNoAccessCurrApp";
//                                this.jdbcTemplate.update(this.sqlInsertLog, new Object[]{uid, account_id, user_name, app_sid, app_name, "2", errMsg, ip, url});
//                                throw new UmeTicketValidationException("authenticationFailure.CurrUserNoAccessCurrApp", errMsg, service);
//                            }
//                        } else {
//                            errMsg = "authenticationFailure.CurrUserNoAccessCurrApp";
//                            this.jdbcTemplate.update(this.sqlInsertLog, new Object[]{uid, account_id, user_name, app_sid, app_name, "2", errMsg, ip, url});
//                            throw new UmeTicketValidationException("authenticationFailure.CurrUserNoAccessCurrApp", errMsg, service);
//                        }
//                    } else {
//                        errMsg = "authenticationFailure.UnmanagedApplication";
//                        this.jdbcTemplate.update(this.sqlInsertLog, new Object[]{uid, account_id, user_name, app_sid, serv, "2", errMsg, ip, url});
//                        throw new UmeTicketValidationException("authenticationFailure.UnmanagedApplication", errMsg, service);
//                    }
//                } else {
//                    errMsg = "authenticationFailure.UnionAccountNotExist";
//                    this.jdbcTemplate.update(this.sqlInsertLog, new Object[]{uid, account_id, user_name, app_sid, app_name, "2", errMsg, ip, url});
//                    throw new UmeTicketValidationException("authenticationFailure.UnionAccountNotExist", errMsg, service);
//                }
//            }
//        }
    }

    private ModelAndView generateErrorView(String code, String description, Object[] args) {
        ModelAndView modelAndView = new ModelAndView(this.failureView);
        String convertedDescription = this.getMessageSourceAccessor().getMessage(description, args, description);
        modelAndView.addObject("code", code);
        modelAndView.addObject("description", convertedDescription);
        return modelAndView;
    }

    private ModelAndView generateSuccessView(Assertion assertion, String proxyIou) {
        ModelAndView success = new ModelAndView(this.successView);
        success.addObject("assertion", assertion);
        success.addObject("pgtIou", proxyIou);
        return success;
    }

    private ValidationSpecification getCommandClass() {
        try {
            return (ValidationSpecification)this.validationSpecificationClass.newInstance();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public boolean canHandle(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    public final void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public final void setArgumentExtractor(ArgumentExtractor argumentExtractor) {
        this.argumentExtractor = argumentExtractor;
    }

    public final void setValidationSpecificationClass(Class<?> validationSpecificationClass) {
        this.validationSpecificationClass = validationSpecificationClass;
    }

    public final void setFailureView(String failureView) {
        this.failureView = failureView;
    }

    public final void setSuccessView(String successView) {
        this.successView = successView;
    }

    public final void setProxyHandler(ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    public final void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }
}
