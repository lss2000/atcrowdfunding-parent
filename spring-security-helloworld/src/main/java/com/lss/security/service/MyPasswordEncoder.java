package com.lss.security.service;

import com.lss.security.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyPasswordEncoder implements PasswordEncoder {
    //为了对密码进行加密
    @Override
    public String encode(CharSequence charSequence) {
        //charSequence  或者rawSequence  ===>原密码（明文）
        return MD5Util.digest(charSequence.toString());
    }


    /**
     *  密文比较   进一步确认密码是否一致
     * @param charSequence   明文
     * @param s===>encodedPassword  密文
     * @return
     */
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return MD5Util.digest(charSequence.toString()).equals(s);
    }
}
