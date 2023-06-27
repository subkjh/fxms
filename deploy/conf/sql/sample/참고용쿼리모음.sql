
---------------------------------------------------------------------------------------------------
-- 관리대상 및 관제점 조회
-- 관리대상과 관리대상이 수집하는 성능을 조회한다.
---------------------------------------------------------------------------------------------------
-- 포인트이용
select a.MO_NO            as MO_NO          /* 관리대상번호 */
       , a.MO_NAME        as MO_NAME        /* 관리대상명 */
       , b.POINT_TID      as MO_INSTANCE    /* 관제점 (화면에 보이지 않고 데이터 조회에만 사용한다.) */
       , b.PS_ID          as PS_ID          /* 성능항목 */
       , b.POINT_NM       as PS_NAME        /* 성능명 */
       , b.POINT_UNIT     as PS_UNIT        /* 성능단위 */
from   FX_MO              a                 /* 관리대상테이블 */
       , FX_PS_POINT      b                 /* 관제점테이블 */
where  exists ( select 1 from FX_CF_INLO_MEM a1 where a1.INLO_NO = 1000 and a1.LOWER_INLO_NO = a.INLO_NO ) /* 설치위치번호 */
and    a.MO_TID       = b.MO_TID
and    b.PS_ID        is not null
order by 
       mo_name
       , point_nm
;
        
-- 성능항목이용
select  a.MO_NO        as MO_NO      /* 관리대상번호 */
        , a.MO_NAME    as MO_NAME    /* 관리대상명 */
        , b.PS_ID      as PS_ID      /* 성능항목 */
        , b.PS_NAME    as PS_NAME    /* 성능명 */
        , b.PS_UNIT    as PS_UNIT    /* 성능단위 */
from    FX_MO          a             /* 관리대상테이블 */
        , FX_PS_ITEM   b             /* 성능항목테이블 */
where   exists ( select 1 from FX_CF_INLO_MEM a1 where a1.INLO_NO = 1000 and a1.LOWER_INLO_NO = a.INLO_NO ) /* 설치위치번호 */
and     b.MO_CLASS = a.MO_CLASS 
and     b.MO_TYPE = a.MO_TYPE 
;


---------------------------------------------------------------------------------------------------
-- 메뉴조회
-- 사용자의 사용자그룹번호를 이용하여 메뉴 목록을 조회한다.
---------------------------------------------------------------------------------------------------
select  a.OP_ID                as MENU_ID
        , b.UPPER_OP_ID        as MENU_GRP_ID
        , ifnull(b.MENU_NAME, a.OP_TITLE)
                               as MENU_NAME
        , b.ICON_NAME          as MENU_ICON
        , a.OP_TYPE_CD         as OP_TYPE_CD
        , a.UI_CMPT_NAME       as UI_COMP_NAME
from    FX_CO_OP               a                 /* 기능테이블 */
        , FX_CO_OP_MENU        b                 /* 메뉴테이블 */
        , FX_UR_USER           c                 /* 사용자테이블 */
        , FX_UR_UGRP           d                 /* 사용자그룹테이블 */
        , FX_UR_UGRP_OP        e                 /* 사용자그룹권한테이블 */
where   a.OP_ID          = b.OP_ID
and     a.USE_YN         = 'Y'
and     c.USER_NO        = 1000                  /* 사용자번호 */
and     d.UGRP_NO        = c.UGRP_NO
and     e.UGRP_NO        = d.UGRP_NO
and     a.OP_ID          = e.OP_ID
and     b.MNG_DIV        = d.MNG_DIV             /* 사이트확인 */
order by b.SORT_SEQ                              /* 순서 지정 */
;
