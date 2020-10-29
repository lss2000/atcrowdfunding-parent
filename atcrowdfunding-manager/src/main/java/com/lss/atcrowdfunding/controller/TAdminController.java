package com.lss.atcrowdfunding.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lss.atcrowdfunding.bean.TAdmin;
import com.lss.atcrowdfunding.bean.TMenu;
import com.lss.atcrowdfunding.service.TAdminService;
import com.lss.atcrowdfunding.service.TMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TAdminController {

    @Autowired
    private TAdminService tAdminService;

    @Autowired
    private TMenuService tMenuService;

    /*@RequestMapping("/login")   //使用security框架提供的/login控制器
    public String login(TAdmin tAdmin, HttpSession session){

        try {
            TAdmin loginAdmin = tAdminService.getTAdmin(tAdmin);
            session.setAttribute("loginAdmin",loginAdmin);
        } catch (Exception e) {
            session.setAttribute("err",e.getMessage());
            return "redirect:welcome.jsp";
        }
        return "redirect:/atcrowfunding/main";//成功重定向到main方法
    }
*/
    /**
     * 灯枯成功跳转到main.jsp
     * @return
     */
    @RequestMapping("/atcrowfunding/main")
    public String main(HttpSession session){

        //获得所有菜单信息
        List<TMenu> tMenus = tMenuService.listMenus();

        //父节点集合,用于存储父节点
        List<TMenu> parentList = new ArrayList<>();
        //获取父节点,储存到parentList集合中
        for(TMenu tMenu:tMenus){
            if(tMenu.getPid()==0){
                parentList.add(tMenu);
            }
        }

        //遍历所有节点，判断是否是子节点
        for(TMenu child:tMenus){
            if(child.getPid()!=0){
                //是子节点，就遍历父节点集合，来看这个子节点属于哪个父节点
                for(TMenu parent:parentList){
                    if(parent.getId()==child.getPid()){
                        parent.getChildMenus().add(child);
                    }

                }

            }
        }

        session.setAttribute("parentList",parentList);

        return "main";//转向到main.jsp
    }

    /*@RequestMapping("/logout")   //使用security框架提供的/logout控制器
    public String logout(HttpSession session){
        if(session!=null){
            //如果session中的用户信息还在，就手动销毁
            session.invalidate();
        }
        return "redirect:welcome.jsp";
    }*/


    @PreAuthorize("hasAnyRole('学徒','大师') ")
    @RequestMapping("/admin/index")
    public String adminIndex(
            @RequestParam(value = "pageNum",defaultValue = "1",required = false) Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "2",required = false) Integer pageSize,
            @RequestParam(value = "keyWord",defaultValue = "",required = false) String keyWord,
            Model model

    ){
        PageHelper.startPage(pageNum,pageSize);//limit ?,?

        List<TAdmin> tAdminList = tAdminService.listTAdminPage(keyWord);

        PageInfo<TAdmin> pageInfo = new PageInfo<>(tAdminList,5);

        model.addAttribute("pageInfo",pageInfo);

        return "admin/adminIndex";
    }

    @RequestMapping("/admin/toAdd")
    public String toAdd(){
        return "admin/adminAdd";
    }

    @RequestMapping("/admin/add")
    public String add(TAdmin tAdmin){
        tAdminService.saveTadmin(tAdmin);
        return "redirect:/admin/index?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/toEdit")
    public String toEdit(Integer id, Map<String,Object> map,Integer pageNum){

        TAdmin tAdmin = tAdminService.getTAdminById(id);

        map.put("tAdmin",tAdmin);

        return "admin/adminEdit";

    }

    @RequestMapping("/admin/edit")
    public String edit(TAdmin tAdmin,Integer pageNum){

        tAdminService.updateTAdmin(tAdmin);

        return "redirect:/admin/index?pageNum="+pageNum;
    }

    @RequestMapping("/admin/delete")
    public String delete(Integer id,Integer pageNum){
        tAdminService.deleteTAdminById(id);
        return "redirect:/admin/index?pageNum="+pageNum;
    }

    @RequestMapping("/admin/deleteBath")
    public String deleteBath(String ids){
        //分割字符串ids成一个String数组
        String[] idStrs = ids.split(",");
        //将数组转成Integer类型的集合
        List<Integer> idInts = new ArrayList<>();
        for(String idStr:idStrs){
            Integer id = Integer.parseInt(idStr);
            idInts.add(id);
        }
        tAdminService.deleteBath(idInts);
        return "redirect:/admin/index";
    }


}
