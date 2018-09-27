package org.jasig.cas.web;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.perf4j.log4j.GraphingStatisticsAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 5:24 PM.
 */
public final class StatisticsController extends AbstractController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int NUMBER_OF_MILLISECONDS_IN_A_DAY = 86400000;
    private static final int NUMBER_OF_MILLISECONDS_IN_AN_HOUR = 3600000;
    private static final int NUMBER_OF_MILLISECONDS_IN_A_MINUTE = 60000;
    private static final int NUMBER_OF_MILLISECONDS_IN_A_SECOND = 1000;
    private final TicketRegistry ticketRegistry;
    private final Date upTimeStartDate = new Date();
    private String casTicketSuffix;

    public StatisticsController(TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }

    public void setCasTicketSuffix(String casTicketSuffix) {
        this.casTicketSuffix = casTicketSuffix;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView("viewStatisticsView");
        modelAndView.addObject("startTime", this.upTimeStartDate);
        double difference = (double)(System.currentTimeMillis() - this.upTimeStartDate.getTime());
        modelAndView.addObject("upTime", this.calculateUptime(difference, new LinkedList(Arrays.asList(86400000, 3600000, 60000, 1000, 1)), new LinkedList(Arrays.asList("day", "hour", "minute", "second", "millisecond"))));
        modelAndView.addObject("totalMemory", Runtime.getRuntime().totalMemory() / 1024L / 1024L);
        modelAndView.addObject("maxMemory", Runtime.getRuntime().maxMemory() / 1024L / 1024L);
        modelAndView.addObject("freeMemory", Runtime.getRuntime().freeMemory() / 1024L / 1024L);
        modelAndView.addObject("availableProcessors", Runtime.getRuntime().availableProcessors());
        modelAndView.addObject("serverHostName", httpServletRequest.getServerName());
        modelAndView.addObject("serverIpAddress", httpServletRequest.getLocalAddr());
        modelAndView.addObject("casTicketSuffix", this.casTicketSuffix);
        int unexpiredTgts = 0;
        int unexpiredSts = 0;
        int expiredTgts = 0;
        int expiredSts = 0;

        Collection appenders;
        try {
            appenders = this.ticketRegistry.getTickets();
            Iterator var11 = appenders.iterator();

            while(var11.hasNext()) {
                Ticket ticket = (Ticket)var11.next();
                if (ticket instanceof ServiceTicket) {
                    if (ticket.isExpired()) {
                        ++expiredSts;
                    } else {
                        ++unexpiredSts;
                    }
                } else if (ticket.isExpired()) {
                    ++expiredTgts;
                } else {
                    ++unexpiredTgts;
                }
            }
        } catch (UnsupportedOperationException var13) {
            this.logger.trace("The ticket registry doesn't support this information.");
        }

        appenders = GraphingStatisticsAppender.getAllGraphingStatisticsAppenders();
        modelAndView.addObject("unexpiredTgts", unexpiredTgts);
        modelAndView.addObject("unexpiredSts", unexpiredSts);
        modelAndView.addObject("expiredTgts", expiredTgts);
        modelAndView.addObject("expiredSts", expiredSts);
        modelAndView.addObject("pageTitle", modelAndView.getViewName());
        modelAndView.addObject("graphingStatisticAppenders", appenders);
        return modelAndView;
    }

    protected String calculateUptime(double difference, Queue<Integer> calculations, Queue<String> labels) {
        if (calculations.isEmpty()) {
            return "";
        } else {
            int value = (Integer)calculations.remove();
            double time = Math.floor(difference / (double)value);
            double newDifference = difference - time * (double)value;
            String currentLabel = (String)labels.remove();
            String label = time != 0.0D && time <= 1.0D ? currentLabel : currentLabel + "s";
            return Integer.toString((new Double(time)).intValue()) + " " + label + " " + this.calculateUptime(newDifference, calculations, labels);
        }
    }
}

