package com.lss.atcrowdfunding.service.impl;

import com.lss.atcrowdfunding.bean.TMenu;
import com.lss.atcrowdfunding.mapper.TMenuMapper;
import com.lss.atcrowdfunding.service.TMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TMenuServiceImpl implements TMenuService {

    @Autowired
    private TMenuMapper tMenuMapper;

    @Override
    public List<TMenu> listMenus() {
        //查询所有的菜单（父节点和子节点都查询）
        List<TMenu> tMenus = tMenuMapper.selectByExample(null);

        return tMenus;
    }
}
