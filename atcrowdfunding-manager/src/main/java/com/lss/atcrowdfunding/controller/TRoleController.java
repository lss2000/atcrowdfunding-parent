package com.lss.atcrowdfunding.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lss.atcrowdfunding.bean.TRole;
import com.lss.atcrowdfunding.service.TRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TRoleController {

    @Autowired
    private TRoleService tRoleService;

    @RequestMapping("/role/index")
    public String index(){
        return "role/roleIndex";
    }

    @PreAuthorize("hasRole('大师')")
    @ResponseBody
    @RequestMapping("/role/loadData")
    public PageInfo<TRole> loadData(
           @RequestParam(value = "pageNum",defaultValue = "1",required = false) Integer pageNum,
           @RequestParam(value = "pageSize",defaultValue = "2",required = false) Integer pageSize,
           @RequestParam(value = "keyWord",defaultValue = "",required = false) String keyWord
    ){
        PageHelper.startPage(pageNum,pageSize);

        List<TRole> tRoles = tRoleService.listTRole(keyWord);

        PageInfo<TRole> pageInfo = new PageInfo<>(tRoles,5);

        return pageInfo;
    }

    @ResponseBody
    @RequestMapping("/role/saveRole")
    public String saveRole(TRole tRole){
        int rows = tRoleService.saveRole(tRole);
        if(rows==1){
            return "yes";
        }else{
            return "no";
        }

    }

    @ResponseBody
    @RequestMapping("/role/getRole")
    public TRole getRole(Integer id){
        return tRoleService.getRole(id);
    }

    @ResponseBody
    @RequestMapping("/role/updateRole")
    public String update(TRole tRole){
        Integer row = tRoleService.updateRole(tRole);
        if(row==1){
            return "yes";
        }else{
            return "no";
        }
    }

    @ResponseBody
    @RequestMapping("role/deleteBath")
    public String deleteBath(String ids){
        String[] idStrs = ids.split(",");
        List<Integer> idsInt = new ArrayList<>();
        for(String idStr:idStrs){
            Integer id = Integer.parseInt(idStr);
            idsInt.add(id);
        }
        int rows = tRoleService.deleteBath(idsInt);

        if(rows>0){
            return "yes";

        }else{
            return "no";
        }

    }

    @ResponseBody
    @RequestMapping("/role/deleteRoleById")
    public String deleteRoleById(Integer id){
        int row = tRoleService.deleteRoleById(id);
        if(row==1){
            return "yes";
        }
        return "no";
    }

}
