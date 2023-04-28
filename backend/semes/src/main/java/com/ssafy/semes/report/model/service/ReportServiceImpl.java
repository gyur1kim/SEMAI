package com.ssafy.semes.report.model.service;

import com.ssafy.semes.exception.JPAException;
import com.ssafy.semes.oht.model.OHTEntity;
import com.ssafy.semes.ohtcheck.model.OHTCheckEntity;
import com.ssafy.semes.report.model.QuestionDto;
import com.ssafy.semes.report.model.ReportListResponseDto;
import com.ssafy.semes.wheelcheck.model.WheelCheckEntity;
import com.ssafy.semes.wheelcheck.model.repository.WheelCheckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@SuppressWarnings("unchecked")
public class ReportServiceImpl implements ReportService {
    @Autowired
    private WheelCheckRepository wheelCheckRepository;
    @Autowired
    private EntityManager em;
    private final int PAGE_SIZE = 1;

    @Override
    @Transactional
    public Map<String,Object> findReport(QuestionDto dto) throws Exception {
        StringTokenizer st = new StringTokenizer(dto.getDate(),"-");
        int yy =  Integer.parseInt(st.nextToken());
        int mm =  Integer.parseInt(st.nextToken());
        int dd= Integer.parseInt(st.nextToken());
        if (dto.getTime().equals("ALL")) {
            dto.setStartTime(LocalDateTime.of(LocalDate.of(yy,mm,dd)
                    , LocalTime.of(0,0,0)));
            dto.setEndTime(LocalDateTime.of(LocalDate.of(yy,mm,dd)
                    , LocalTime.of(23,59,59)));
        } else {
            dto.setStartTime(LocalDateTime.of(LocalDate.of(yy,mm,dd)
                    , LocalTime.of(Integer.parseInt(dto.getTime()),0,0)));
            dto.setEndTime(LocalDateTime.of(LocalDate.of(yy,mm,dd)
                    , LocalTime.of(Integer.parseInt(dto.getTime()),59,59)));
        }

        dto.setPage((dto.getPage() - 1) * PAGE_SIZE);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e FROM WheelCheckEntity e join fetch e.ohtCheck oe join fetch oe.oht")
                .append(" where (e.checkDate BETWEEN :start and :end)");

        if (!dto.getOhtSn().equals("ALL")) {
            sb.append("and o.ohtSN = :sn ");
        }
        if (!dto.getWheelPosition().equals("ALL")) {
            sb.append("and e.wheelPosition = :position");
        }
        //sb.append(" limit :size offset :page");
        Query query = em.createQuery(sb.toString(), WheelCheckEntity.class);

        query.setParameter("start", dto.getStartTime());
        query.setParameter("end", dto.getEndTime());
        if (!dto.getOhtSn().equals("ALL")) {
            query.setParameter("sn", dto.getOhtSn());
        }
        if (!dto.getWheelPosition().equals("ALL")) {
            query.setParameter("position", dto.getWheelPosition());
        }
        long totalPage = query.getResultStream().count();
        query.setFirstResult(dto.getPage());
        query.setMaxResults(PAGE_SIZE);
        List<WheelCheckEntity> list = query.getResultList();

        if (list == null) {
            throw new JPAException();
        }
        Map<String,Object> ruturnObj = new HashMap();

        ruturnObj.put("result",list.stream().map(m -> {
            return ReportListResponseDto.builder()
                    .ohtSn(m.getOhtCheck().getOht().getOhtSN())
                    .boltGoodCount(m.getBoltGoodCount())
                    .wheelCheckDate(m.getCheckDate())
                    .wheelChcekId(m.getWheelHistoryId())
                    .wheelPosition(m.getWheelPosition())
                    .build();
        }).collect(Collectors.toList()));
        ruturnObj.put("totalPage",totalPage);
        return ruturnObj;
    }

    @Override
    @Transactional
    public ReportListResponseDto findReportDetail(long wheelChcekId) throws Exception {
        WheelCheckEntity wheel = wheelCheckRepository.findByWheelHistoryId(wheelChcekId);
        if (wheel == null) {
            throw new JPAException();
        }
        log.info("WheelCheckEntity : " + wheel);
        return ReportListResponseDto.builder()
                .ohtSn(wheel.getOhtCheck().getOht().getOhtSN())
                .boltGoodCount(wheel.getBoltGoodCount())
                .wheelCheckDate(wheel.getCheckDate())
                .wheelChcekId(wheel.getWheelHistoryId())
                .wheelPosition(wheel.getWheelPosition())
                .build();
    }
}
