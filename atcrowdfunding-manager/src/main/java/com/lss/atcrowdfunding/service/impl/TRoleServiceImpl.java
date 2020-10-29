package com.lss.atcrowdfunding.service.impl;

import com.lss.atcrowdfunding.bean.TRole;
import com.lss.atcrowdfunding.bean.TRoleExample;
import com.lss.atcrowdfunding.mapper.TRoleMapper;
import com.lss.atcrowdfunding.service.TRoleService;
import com.lss.atcrowdfunding.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TRoleServiceImpl implements TRoleService {
    @Autowired
    private TRoleMapper tRoleMapper;


    @Override
    public List<TRole> listTRole(String keyWord) {

        TRoleExample example = new TRoleExample();

        if(StringUtil.isNotEmpty(keyWord)){
            TRoleExample.Criteria criteria = example.createCriteria();
            criteria.andNameLike("%"+keyWord+"%");
        }
        return tRoleMapper.selectByExample(example);
    }

    @Override
    public int saveRole(TRole tRole) {

        return tRoleMapper.insertSelective(tRole);
    }

    @Override
    public TRole getRole(Integer id) {
        return tRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateRole(TRole tRole) {

        return tRoleMapper.updateByPrimaryKeySelective(tRole);
    }

    @Override
    public int deleteBath(List<Integer> idsInt) {
        TRoleExample example = new TRoleExample();
        TRoleExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(idsInt);

        return tRoleMapper.deleteByExample(example);
    }

    @Override
    public int deleteRoleById(Integer id) {

        return tRoleMapper.deleteByPrimaryKey(id);
    }
}
