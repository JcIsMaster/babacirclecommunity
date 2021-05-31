package com.example.babacirclecommunity.collaborate.service.impl;

import com.example.babacirclecommunity.collaborate.service.ICollaborateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MQ
 * @date 2021/5/31 10:47
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CollaborateServiceImpl implements ICollaborateService {


}
