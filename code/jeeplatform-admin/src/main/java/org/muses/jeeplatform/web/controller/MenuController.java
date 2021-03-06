package org.muses.jeeplatform.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.muses.jeeplatform.core.Constants;
import org.muses.jeeplatform.model.entity.Menu;
import org.muses.jeeplatform.repository.MenuTreeRepository;
import org.muses.jeeplatform.service.MenuService;
import org.muses.jeeplatform.service.MenuTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by op.43027 on 2017/5/27 0027.
 */
@RequestMapping("/menu")
@Controller
public class MenuController extends BaseController {

    @Autowired
    MenuService menuService;
//    @Autowired
//    MenuTreeService menuTreeService;

     @RequestMapping(value = "/getMenus", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ModelAndView toMenuList(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
         String pageIndexStr = request.getParameter("pageIndex");

         int pageSize = Constants.PAGE_SIZE;
         ModelAndView mv = this.getModelAndView();
         Page<Menu> menuPage;

         if(pageIndexStr==null||"".equals(pageIndexStr)){
            pageIndexStr = "0";
         }

         int pageIndex = Integer.parseInt(pageIndexStr);

         menuPage = menuService.findAll(pageIndex+1, pageSize, Sort.Direction.ASC,"menuId");
         mv.addObject("totalCount",menuPage.getTotalElements());
         mv.addObject("pageIndex",pageIndex);
         JSONArray jsonData = JSONArray.fromObject(menuPage.getContent());
         mv.addObject("menus",jsonData.toString());

         mv.setViewName("admin/menu/menu_list");
         return mv;
    }

    @RequestMapping(value = "/loadMenus", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void doLoadData(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        String pageIndexStr = request.getParameter("pageIndex");
        if(pageIndexStr==null||"".equals(pageIndexStr)){
            pageIndexStr = "1";
        }
        //System.out.println(pageString);
        int pageIndex = Integer.parseInt(pageIndexStr);
        if(pageIndex == 0){
            pageIndex = 1;
        }

        int pageSize = Constants.PAGE_SIZE;
        Page<Menu> menuPage = menuService.findAll(pageIndex, pageSize, Sort.Direction.ASC,"menuId");
        JSONArray jsonData = JSONArray.fromObject(menuPage.getContent());

        PrintWriter out;

        response.setCharacterEncoding("utf-8");
        out = response.getWriter();
        out.write(jsonData.toString());
        out.flush();
        out.close();

    }

    @RequestMapping(value="")
    @ResponseBody
    public String queryM(){

         return "";
    }

    /**
     * 获取当前菜单的所有子菜单
     * @param menuId
     * @param response
     */
    @RequestMapping(value="/sub")
    public void getSub(@RequestParam String menuId, HttpServletResponse response)throws Exception{
        try {
            List<Menu> subMenu = menuService.findSubMenuById(Integer.parseInt(menuId));
            JSONArray arr = JSONArray.fromObject(subMenu);
            PrintWriter out;

            response.setCharacterEncoding("utf-8");
            out = response.getWriter();
            String json = arr.toString();
            out.write(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    @RequestMapping(value="/goEditM",method = RequestMethod.GET)
    public String goEditM(HttpServletRequest request,Model model){
        String menuIdStr = request.getParameter("menuId");
        int menuId = Integer.parseInt(menuIdStr);
        Menu menu = menuService.findMenuById(menuId);
        model.addAttribute("menu",menu);
        return "admin/menu/menu_edit";
    }

}