package com.coding.sales.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.alibaba.fastjson.JSON;
import com.coding.sales.FileUtils;

/**
 * 客户信息
 * 
 * @author Kick_
 *
 */
public class Member {

	private String memberNo;

	private String memberName;

	private String oldMemberType;

	private String newMemberType;

	private int memberPointsIncreased;

	private int memberPoints;

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getOldMemberType() {
		return oldMemberType;
	}

	public void setOldMemberType(String oldMemberType) {
		this.oldMemberType = oldMemberType;
	}

	public String getNewMemberType() {
		return newMemberType;
	}

	public void setNewMemberType(String newMemberType) {
		this.newMemberType = newMemberType;
	}

	

	public int getMemberPointsIncreased() {
		return memberPointsIncreased;
	}

	public void setMemberPointsIncreased(int memberPointsIncreased) {
		this.memberPointsIncreased = memberPointsIncreased;
	}

	public int getMemberPoints() {
		return memberPoints;
	}

	public void setMemberPoints(int memberPoints) {
		this.memberPoints = memberPoints;
	}

	public static Member getMemberByNo(String memberNo) throws Exception {
		Member mb = new Member();
		String jsonMembers = "[{'memberNo':'6236609999','memberName':'马丁','memberType':'普卡','memberPoints':'9860'},{'memberNo':'6630009999','memberName':'王立','memberType':'金卡','memberPoints':'48860'},{'memberNo':'8230009999','memberName':'李想','memberType':'白金卡','memberPoints':'98860'},{'memberNo':'9230009999','memberName':'张三','memberType':'钻石卡','memberPoints':'198860'}]";
		List<Member> members = mb.getMembers(jsonMembers, Member.class);
		if (members.size() > 0) {
			for (int i = 0; i < members.size(); i++) {
				if (members.get(i).getMemberNo().equals(memberNo)) {
					members.get(i).setOldMemberType(getMemberType(members.get(i).getMemberPoints()+members.get(i).getMemberPointsIncreased()));
					mb = members.get(i);
					break;
				}
			}
		} else {
			throw new Exception("客户信息不存在");
		}
		if (mb == null) {
			throw new Exception("客户信息不存在");
		}

		return mb;
	};
	
	
	/** 判断客户等级
	 * @param Points
	 * @return
	 */
	public static String getMemberType(int Points) {
		String memberType="普卡";
		if(Points<10000) {
			memberType="普卡";
		}else if(Points>=10000&&Points<50000) {
			memberType="金卡";
		}else if(Points>=50000&&Points<100000) {
			memberType="白金卡";
		}else if(Points>=100000) {
			memberType="钻石卡";
		}else {
			memberType="普卡";
		}
		
		return memberType;
		
	}
	


	public static <T> T getMember(String jsonString, Class cls) {
		T t = null;
		try {
			t = (T) JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	public static <T> List<T> getMembers(String jsonString, Class cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(jsonString, cls);
		} catch (Exception e) {
		}
		return list;
	}

}
