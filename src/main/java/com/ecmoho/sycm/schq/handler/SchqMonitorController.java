package com.ecmoho.sycm.schq.handler;



import com.ecmoho.sycm.schq.service.SpiderSchqService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//页面控制器
@Controller
@RequestMapping(value="/schqmonitor")
public class SchqMonitorController {

    @Resource(name = "spiderSchqServiceImpl")
    private SpiderSchqService spiderSchqService;

    @RequestMapping(value = "startgrap/{accountid}/{accountChildid}/{dateStr}",method = RequestMethod.GET)
    @ResponseBody
    public String stratGrap(HttpServletRequest request, @PathVariable("accountid") int accountid,
                          @PathVariable("accountChildid") int accountChildid,@PathVariable("dateStr") String dateStr){
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        spiderSchqService.startGrap(wac,accountid,accountChildid,dateStr);
        System.out.println("accountid:"+accountid+",accountChildid:"+accountChildid+",dateStr:"+dateStr+",Done!!");
        return "success";
    }
}
