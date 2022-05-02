package re.jpayet.mentdb.ext.se;

import java.util.ArrayList;
import java.util.Collections;

import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.MathFx;

public class Entity {

	public String en = "";
	public String fr = "";
	public long id = 0;
	public byte where = 0;
	public byte intensity = 0;
	String dispoj1 = "", dispoj2 = "", dispoj3 = "", dispoj4 = "", dispoj5 = "";
	String dispoh1 = "", dispoh2 = "", dispoh3 = "", dispoh4 = "", dispoh5 = "";
	String dt_depart = "", dt_fin = "";
	
	public Entity(long id, String en, String fr, byte where, byte intensity
			, String dispoj1, String dispoh1
			, String dispoj2, String dispoh2
			, String dispoj3, String dispoh3
			, String dispoj4, String dispoh4
			, String dispoj5, String dispoh5
			, String dt_depart, String dt_fin) {

		this.id = id;
		this.en = en;
		this.fr = fr;
		this.where = where;
		this.intensity = intensity;
		this.dispoj1 = dispoj1;
		this.dispoj2 = dispoj2;
		this.dispoj3 = dispoj3;
		this.dispoj4 = dispoj4;
		this.dispoj5 = dispoj5;
		this.dispoh1 = dispoh1;
		this.dispoh2 = dispoh2;
		this.dispoh3 = dispoh3;
		this.dispoh4 = dispoh4;
		this.dispoh5 = dispoh5;
		this.dt_depart = dt_depart;
		this.dt_fin = dt_fin;
		
	}
	
	public long get_dispo(String sysdate, String systime) throws Exception {
		
		long min = Long.parseLong(DateFx.format(sysdate+" "+systime, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmm"));
		
		if (dt_depart==null || dt_depart.equals("")) {
			
			if (dt_fin==null || dt_fin.equals("")) {
				
				return get_dispo_in(sysdate, systime, min);
				
			} else {
				
				long min_dt_fin = Long.parseLong(DateFx.format(dt_fin, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmm"));
				if (min<min_dt_fin) {
					return get_dispo_in(sysdate, systime, min);
				} else {
					return 230001010000L;
				}
				
			}
			
		} else {

			long min_dt_depart = Long.parseLong(DateFx.format(dt_depart, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmm"));
			if (dt_fin==null || dt_fin.equals("")) {
				
				if (min<min_dt_depart) {
					return min_dt_depart;
				} else {
					return get_dispo_in(sysdate, systime, min);
				}
				
			} else {

				long min_dt_fin = Long.parseLong(DateFx.format(dt_fin, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmm"));
				
				if (min<min_dt_depart) {
					return min_dt_depart;
				} {
					if (min<min_dt_fin) {
						return get_dispo_in(sysdate, systime, min);
					} {
						return 230001010000L;
					}
				}
				
			}
			
		}
		
	}
	
	public long get_dispo_in(String sysdate, String systime, long min) throws Exception {
		
		int current_week_day = Integer.parseInt(DateFx.day_of_week(sysdate))-1;
		ArrayList<Long> dt = new ArrayList<Long>();
		for(int iZ=0;iZ<=7;iZ++) {
			int cur_week_day = Integer.parseInt(MathFx.mod(""+(current_week_day+iZ), "7"))+1;
			if (dispoj1!=null && !dispoj1.equals("")) {
				if (Integer.parseInt(AtomFx.find(dispoj1, cur_week_day+"", ","))>0) {
					if ((!AtomFx.get(dispoh1, "1", ",").equals("00")) || (!AtomFx.get(dispoh1, "2", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh1, "1", ",")+AtomFx.get(dispoh1, "2", ",")));
					}
					if ((!AtomFx.get(dispoh1, "5", ",").equals("00")) || (!AtomFx.get(dispoh1, "6", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh1, "5", ",")+AtomFx.get(dispoh1, "6", ",")));
					}
				}
			}
			if (dispoj2!=null && !dispoj2.equals("")) {
				if (Integer.parseInt(AtomFx.find(dispoj2, cur_week_day+"", ","))>0) {
					if ((!AtomFx.get(dispoh2, "1", ",").equals("00")) || (!AtomFx.get(dispoh2, "2", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh2, "1", ",")+AtomFx.get(dispoh2, "2", ",")));
					}
					if ((!AtomFx.get(dispoh2, "5", ",").equals("00")) || (!AtomFx.get(dispoh2, "6", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh2, "5", ",")+AtomFx.get(dispoh2, "6", ",")));
					}
				}
			}
			if (dispoj3!=null && !dispoj3.equals("")) {
				if (Integer.parseInt(AtomFx.find(dispoj3, cur_week_day+"", ","))>0) {
					if ((!AtomFx.get(dispoh3, "1", ",").equals("00")) || (!AtomFx.get(dispoh3, "2", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh3, "1", ",")+AtomFx.get(dispoh3, "2", ",")));
					}
					if ((!AtomFx.get(dispoh3, "5", ",").equals("00")) || (!AtomFx.get(dispoh3, "6", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh3, "5", ",")+AtomFx.get(dispoh3, "6", ",")));
					}
				}
			}
			if (dispoj4!=null && !dispoj4.equals("")) {
				if (Integer.parseInt(AtomFx.find(dispoj4, cur_week_day+"", ","))>0) {
					if ((!AtomFx.get(dispoh4, "1", ",").equals("00")) || (!AtomFx.get(dispoh4, "2", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh4, "1", ",")+AtomFx.get(dispoh4, "2", ",")));
					}
					if ((!AtomFx.get(dispoh4, "5", ",").equals("00")) || (!AtomFx.get(dispoh4, "6", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh4, "5", ",")+AtomFx.get(dispoh4, "6", ",")));
					}
				}
			}
			if (dispoj5!=null && !dispoj5.equals("")) {
				if (Integer.parseInt(AtomFx.find(dispoj5, cur_week_day+"", ","))>0) {
					if ((!AtomFx.get(dispoh5, "1", ",").equals("00")) || (!AtomFx.get(dispoh5, "2", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh5, "1", ",")+AtomFx.get(dispoh5, "2", ",")));
					}
					if ((!AtomFx.get(dispoh5, "5", ",").equals("00")) || (!AtomFx.get(dispoh5, "6", ",").equals("00"))) {
						dt.add(Long.parseLong(DateFx.add(sysdate, "DAY", iZ+"").replace("-", "")+AtomFx.get(dispoh5, "5", ",")+AtomFx.get(dispoh5, "6", ",")));
					}
				}
			}
		}
		
		if (dt.size()>0) {
			Collections.sort( dt );
		}
		
		for(long val : dt) {
			if (val>min) {
				return val;
			}
		}
		
		return 230001010000L;
		
	}
	
	/*
	 * -> "[sysdate]" (date sysdate);
	-> "[systime]" (date systime);
	-> "[min]" (date format (concat [sysdate] " " [systime]) "yyyy-MM-dd HH:mm:ss" "yyyyMMddHHmm");
	-> "[current_week_day]" (- (date day_of_week [sysdate]) 1);
	json load "dt" "[]";
	for (-> "[iZ]" 0) (<= [iZ] 6) (++ "[iZ]") {
		-> "[cur_week_day]" (+ 1 (math mod (+ [current_week_day] [iZ]) 7));
		for (-> "[iDispo]" 1) (<= [iDispo] 5) (++ "[iDispo]") {
			-> "[dispo]" (env get var (concat "[dispoj" [iDispo] "]"));
			-> "[heure]" (env get var (concat "[dispoh" [iDispo] "]"));
			if (not (is null or empty [dispo])) {
				if (> (atom find [dispo] [cur_week_day] ",") 0) {
					if (or (not equal (atom get [heure] 1 ",") "00") (not equal (atom get [heure] 2 ",") "00")) {json iarray "dt" / (concat (string replace (date add [sysdate] "DAY" [iZ]) "-" "") (atom get [heure] 1 ",") (atom get [heure] 2 ",")) STR;};
					if (or (not equal (atom get [heure] 5 ",") "00") (not equal (atom get [heure] 6 ",") "00")) {json iarray "dt" / (concat (string replace (date add [sysdate] "DAY" [iZ]) "-" "") (atom get [heure] 5 ",") (atom get [heure] 6 ",")) STR;};
				};
			};
		};
	};
	json load "dt" (string order (json doc "dt"));
	-> "[result]" "";
	json parse_array "dt" "/" "[val]" {
		if (> [val] [min]) {
			-> "[result]" [val];
			break;
		};
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 */

}
