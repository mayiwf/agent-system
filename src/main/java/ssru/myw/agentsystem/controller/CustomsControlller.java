package ssru.myw.agentsystem.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ssru.myw.agentsystem.entity.Contacts;
import ssru.myw.agentsystem.entity.Customs;
import ssru.myw.agentsystem.entity.User;
import ssru.myw.agentsystem.service.customs.CustomsService;
import ssru.myw.agentsystem.util.PageNumber;
import ssru.myw.agentsystem.util.StringUtil;
import ssru.myw.agentsystem.util.memonto.PageNumberCareTaker;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mayiwen
 * @date: 2018/11/21
 */
@RestController
@RequestMapping("/customs")
public class CustomsControlller {
    @Autowired
    private CustomsService customsService;

    /**
     * 去customs列表页面的方法
     * @param mv
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView toPage(ModelAndView mv) {
        mv.setViewName("customs");
        return mv;
    }

    /**
     * 获得customs列表的json数据
     * @param customs
     * @return
     */
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public String listCustomsJson(Customs customs) {
        PageNumberCareTaker pageNumberCareTaker = new PageNumberCareTaker();
        List<Customs> list = new ArrayList<>();
        list = customsService.  listCustoms(customs,pageNumberCareTaker);
        PageNumber pageNumber = new PageNumber();
        pageNumber.setMemento(pageNumberCareTaker.getMemento());
        Map map = new HashMap();
        map.put("list", list);
        map.put("pageNumber", pageNumber);
        return JSON.toJSONString(map);
    }

    /**
     * 到添加页面的方法
     * @param mv
     * @return
     */
    @RequestMapping(value = "/toAddPage", method = RequestMethod.GET)
    public ModelAndView toAddPage(ModelAndView mv) {
        mv.setViewName("addcustom");
        return mv;
    }

    /**
     * 保存customs的方法
     * @param customs
     * @param session
     * @return
     */
    @PostMapping("")
    public String saveCustoms(@RequestBody Customs customs, HttpSession session) {
        User user = (User) session.getAttribute("user");
        customs.setAgentId(user.getId());
        customs.setAgentCode(user.getUserCode());
        customs.setAgentName(user.getUserName());
        String returnMessage = "";
        Integer mybatisReturn = customsService.saveCustoms(customs);
        return  StringUtil.getInstance().mybatisReturnMessage(mybatisReturn, null);
    }


    /**
     * 通过id获得单个的customs，然后到前端页面。
     * 跳转页面
     * @param mv
     * @return
     */
    @GetMapping("/id/{urlId}")
    public ModelAndView toViewCustomsPage(ModelAndView mv, @PathVariable Integer urlId) {
        // 根据id获得页面的信息
        mv.setViewName("viewcustom");
        mv.addObject("urlId", urlId);
        return mv;
    }

    @GetMapping("/id/json")
    public String getCustoms(Customs customs) {
        customs = customsService.getCustoms(customs);
        return JSON.toJSONString(customs);
    }
    @GetMapping("/view/{urlId}")
    public ModelAndView toUpdateCustomsPage(ModelAndView mv, @PathVariable Integer urlId) {
        mv.setViewName("modifycustom");
        mv.addObject("urlId", urlId);
        return mv;
    }


    @PutMapping("")
    public String updateCustoms(@RequestBody Customs customs) {
        int returnMessage = 0;
        returnMessage = customsService.updateCustoms(customs);
        return StringUtil.getInstance().mybatisReturnMessage(returnMessage, null);
    }


}
