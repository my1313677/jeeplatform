package org.muses.jeeplatform.service;

import org.muses.jeeplatform.model.entity.Menu;
import org.muses.jeeplatform.repository.MenuTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nicky on 2017/6/17.
 */
@Service
public class MenuTreeService {

    @Autowired
    MenuTreeRepository menuTreeRepository;

    /**
     * 查询所有的菜单
     * @return
     */
    @Transactional
    public List<Menu> findAll(){
        return menuTreeRepository.findAll();
    }

}
