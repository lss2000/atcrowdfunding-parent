package com.lss.atcrowdfunding.service;

import com.lss.atcrowdfunding.bean.TAdmin;

import java.util.List;

public interface TAdminService {
    public TAdmin getTAdmin(TAdmin tAdmin);

    //分页  查询  首页显示
    public List<TAdmin> listTAdminPage(String keyWord);

    public void saveTadmin(TAdmin tAdmin);

    TAdmin getTAdminById(Integer id);

    void updateTAdmin(TAdmin tAdmin);

    void deleteTAdminById(Integer id);

    void deleteBath(List<Integer> idInts);
}
