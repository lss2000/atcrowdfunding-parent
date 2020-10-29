package com.lss.atcrowdfunding.service;

import com.lss.atcrowdfunding.bean.TMenu;

import java.util.List;

public interface TMenuService {

    //查询所有的菜单（父节点和子节点都查询出来）
    public List<TMenu> listMenus();
}
