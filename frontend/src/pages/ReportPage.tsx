import React, {useCallback, useState} from 'react';
import { Form, useSearchParams } from "react-router-dom";
import styled from "styled-components";
import { useSelector } from "react-redux";

import { RootState } from "../_store/store";
import { ReportDetailType } from "../_utils/Types";
import {useBodyScrollLock} from "../_hooks/useBodyScrollLock";

import { Button, SemesButton } from "../components/ButtonComponents";
import { Label } from "../components/ReportPage/FilterComponents"
import ReportTable from "../components/ReportPage/ReportTable";
import Title from "../components/Title";
import ReportModal from "../components/DetailModal/ReportModal";

const ReportSection = styled.section`
  padding: 30px;
  display: flex;
  flex-direction: column;
`

let today = new Date();
let year = String(today.getFullYear());
let month = String(today.getMonth() + 1).padStart(2, "0");
let day = String(today.getDate()).padStart(2, "0");
let todayDate = `${year}-${month}-${day}`;

function ReportPage() {
  let [query] = useSearchParams();
  let [calendarDate, setCalendarDate] = useState<string>(query.get('date') || todayDate);

  let [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  let [scrollY, setScrollY] = useState<number>(0);
  let [detailInfo, setDetailInfo] = useState<ReportDetailType>({});        // 선택한 레포트의 상세내역을 전달할 객체
  const { lockScroll, openScroll } = useBodyScrollLock();

  let theme = useSelector((state:RootState) => state.theme.theme);

  /** 달력에서 날짜를 클릭하면 변하는 함수 */
  const handleChange = (e:any) => {
    setCalendarDate(e.target.value);
  }

  /** 모달이 열리면 실행되는 함수 */
  const handleModalOpen = useCallback((detailInfo: ReportDetailType) => {
    setScrollY(window.scrollY);
    setIsModalOpen(true);
    setDetailInfo(detailInfo);
    lockScroll();
  }, [lockScroll])

  const handleModalClose = useCallback(() => {
    setIsModalOpen(false);
    setDetailInfo({});
    openScroll();
  }, [openScroll]);

  return (
    <ReportSection>

      { isModalOpen && <ReportModal scrollY={scrollY} detailInfo={detailInfo} handleModalClose={handleModalClose}  /> }

      <Title title="레포트" />

      <Form style={{height :"30px", marginBottom: "15px", display: "flex", justifyContent: "space-between"}}>
        <div>
          <Label theme={theme}> 장비 종류
            <select name="ohtSn">
              <option value="ALL">전체</option>
              <option value="V30001">V30001</option>
              <option value="V30002">V30002</option>
              <option value="V30003">V30003</option>
            </select>
          </Label>
          <Label theme={theme}> 검사 일자
            <input type="date" value={calendarDate} name="date" max={todayDate} onChange={e => handleChange(e)} />
          </Label>
          <Label theme={theme}> 검사 시간
            <select name="time">
              <option value="ALL">전체</option>
              <option value="0">00:00</option>
              <option value="1">01:00</option>
              <option value="2">02:00</option>
              <option value="23">23:00</option>
            </select>
          </Label>
          <Label theme={theme}> 검사 휠 위치
            <select name="wheelPosition">
              <option value="ALL">전체</option>
              <option value="FL">FL</option>
              <option value="FR">FR</option>
              <option value="RL">RL</option>
              <option value="RR">RR</option>
            </select>
          </Label>
          <SemesButton width="90px" height="100%" type="submit">조회하기</SemesButton>
        </div>
        <div>
          <Button width="90px" height="100%">CSV 출력</Button>
        </div>
      </Form>

      <ReportTable handleModalOpen={handleModalOpen} />
    </ReportSection>
  );
}

export default ReportPage;