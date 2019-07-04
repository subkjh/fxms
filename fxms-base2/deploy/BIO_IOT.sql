-------------------------------------------------------------------------------------
-- 회사 조회
-------------------------------------------------------------------------------------
select *
from FX_CF_INLO
where inlo_name = '전북대학교'
;

-------------------------------------------------------------------------------------
-- 전북대 컨테이너 조회
-------------------------------------------------------------------------------------
select *
from FX_MO A
		, FX_MO_CONTAINER B
where	B.INLO_NO in ( 
			select INLO_NO
			from FX_CF_INLO
			where inlo_name = '전북대학교'
		)
and	A.MO_NO = B.MO_NO
;

-------------------------------------------------------------------------------------
-- 전북대 컨테이너 PBR 조회
-------------------------------------------------------------------------------------
select *
from FX_MO A
		, FX_MO_PBR B
where	A.UPPER_MO_NO in ( 
			select MO_NO
			from  FX_MO_CONTAINER
			where INLO_NO in ( 
						select INLO_NO
						from FX_CF_INLO
						where inlo_name = '전북대학교'
					)
		)
and	A.MO_NO = B.MO_NO
;

-------------------------------------------------------------------------------------
-- 디바이스(센서)조회
-------------------------------------------------------------------------------------

select A.MO_NO				"센서관리번호"	
		, A.MO_NAME			"센서명"
		, B.DEVICE_TYPE			"센서종류"
		, A.UPPER_MO_NO		"컨테이너관리번호"
		, ( select MO_NAME from FX_MO where MO_NO = A.UPPER_MO_NO)
									"컨테이너명"
		, '컨테이너센서'			"비고"
from FX_MO A
		, FX_MO_DEVICE B
where	A.UPPER_MO_NO in ( 
			select MO_NO
			from  FX_MO_CONTAINER
			where INLO_NO in ( 
						select INLO_NO
						from FX_CF_INLO
						where inlo_name = '전북대학교'
					)
		)
and	A.MO_NO = B.MO_NO

union all

select A.MO_NO				"센서관리번호"	
		, A.MO_NAME			"센서명"
		, B.DEVICE_TYPE			"센서종류"
		, A.UPPER_MO_NO		"PBR관리번호"
		, ( select MO_NAME from FX_MO where MO_NO = A.UPPER_MO_NO)
									"PBR명"
		, 'PBR센서'				"비고"
from FX_MO A
		, FX_MO_DEVICE B
where	A.UPPER_MO_NO in ( 
			select MO_NO
			from	FX_MO
			where MO_CLASS = 'PBR'
			and  	UPPER_MO_NO in ( 
						select MO_NO
						from  FX_MO_CONTAINER
						where INLO_NO in ( 
									select INLO_NO
									from FX_CF_INLO
									where inlo_name = '전북대학교'
								)
						)
		)
and	A.MO_NO = B.MO_NO
;

-------------------------------------------------------------------------------------
-- 성능 조회
-------------------------------------------------------------------------------------
select PS_CODE				"성능코드"
		, PS_NAME			"성능명"
		, PS_TABLE			"성능기록테이블"
		, PS_COLUMN		"성능기록컬럼"
from FX_PS_ITEM
;

-- 성능 조회
select	MO_NO				"센서관리번호"
		, PS_DATE			"수집일시"
		, ILLUMI				"조도"
from	FX_V_SNS2_RAW_20170929
order by MO_NO, PS_DATE desc
;


-------------------------------------------------------------------------------------
-- 이벤트 조회
-------------------------------------------------------------------------------------

-- 현재

select *
from 	FX_AL_CUR
;

-- 이력

select *
from 	FX_AL_HST
;
