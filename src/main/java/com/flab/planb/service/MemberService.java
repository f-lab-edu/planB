package com.flab.planb.service;

import com.flab.planb.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MemberMapper memberMapper;

}
