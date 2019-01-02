package fxms.client;

public class ClientCode {

	public enum psType {
		raw, min5, hour1, day1;
	}

	public enum psCode {
		TEMP, PH, CO2, PUMP_ONOFF, PUMP_HZ, LED, DOOR_STATE, CO2_REMAIN, BIOMASS_OUT, ASTAZAN, BIOMASS_IN, COST, ILLUMI, HUMI;
	}

	public enum moClass {
		DEVICE, CONTAINER, GW, PBR;
	}
	
	public enum containerType {
		ft12A("12ft-A"), ft12B("12ft-B"), ft24A("24ft-A"), ft24B("24ft-B");
		
		private String code;
		
		private containerType(String code)
		{
			this.code = code;
		}
		
		public String getCode()
		{
			return code;
		}
	}
	
	public enum inloType
	{
		COUNTRY, COMPANY, PLANT
	}
	
	
	public enum deviceType
	{
		TEMP, PH, CO2
	}
	
	
}
