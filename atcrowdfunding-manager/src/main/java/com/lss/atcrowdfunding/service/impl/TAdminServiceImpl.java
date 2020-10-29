package com.lss.atcrowdfunding.service.impl;

import com.lss.atcrowdfunding.bean.TAdmin;
import com.lss.atcrowdfunding.bean.TAdminExample;
import com.lss.atcrowdfunding.mapper.TAdminMapper;
import com.lss.atcrowdfunding.service.TAdminService;
import com.lss.atcrowdfunding.util.Const;
import com.lss.atcrowdfunding.util.DateUtil;
import com.lss.atcrowdfunding.util.MD5Util;
import com.lss.atcrowdfunding.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TAdminServiceImpl implements TAdminService {

    @Autowired
    private TAdminMapper tAdminMapper;
    @Override
    public TAdmin getTAdmin(TAdmin tAdmin) {

        //创建查询条件
        TAdminExample example = new TAdminExample();
        TAdminExample.Criteria criteria=example.createCriteria();
        criteria.andLoginacctEqualTo(tAdmin.getLoginacct());
        criteria.andUserpswdEqualTo(MD5Util.digest(tAdmin.getUserpswd()));

        //把查询结果放到List集合中
        List<TAdmin> tAdmins = tAdminMapper.selectByExample(example);

        if (tAdmins==null||tAdmins.size()==0){
            throw new RuntimeException("账号或密码错误");
        }
        return tAdmins.get(0);
    }

    @Override
    public List<TAdmin> listTAdminPage(String keyWord) {

        TAdminExample example = new TAdminExample();
        if(StringUtil.isNotEmpty(keyWord)){//如果需要模糊查询
            TAdminExample.Criteria criteria1 = example.createCriteria();
            criteria1.andLoginacctLike("%"+keyWord+"%");
            TAdminExample.Criteria criteria2 = example.createCriteria();
            criteria2.andUsernameLike("%"+keyWord+"%");
            TAdminExample.Criteria criteria3 = example.createCriteria();
            criteria3.andEmailLike("%"+keyWord+"%");

            example.or(criteria2);
            example.or(criteria3);

        }

        List<TAdmin> tAdmins = tAdminMapper.selectByExample(example);//查询条件之后的结果（如果没有keyWord条件查询，则为null==>查询全部）

        return tAdmins;
    }

    @Override
    public void saveTadmin(TAdmin tAdmin) {
        tAdmin.setUserpswd(new BCryptPasswordEncoder().encode(Const.DEFALUT_PASSWORD));
        tAdmin.setCreatetime(DateUtil.getFormatTime());
        tAdminMapper.insertSelective(tAdmin);
    }

    @Override
    public TAdmin getTAdminById(Integer id) {
       return tAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateTAdmin(TAdmin tAdmin) {
        tAdminMapper.updateByPrimaryKeySelective(tAdmin);
    }

    @Override
    public void deleteTAdminById(Integer id) {
        tAdminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteBath(List<Integer> idInts) {

        TAdminExample example = new TAdminExample();
        TAdminExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(idInts);

        tAdminMapper.deleteByExample(example);
    }
}
