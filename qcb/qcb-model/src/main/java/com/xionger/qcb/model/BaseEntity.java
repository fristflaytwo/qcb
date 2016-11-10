package com.xionger.qcb.model;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * entity 父类
 * 
 * @author leo
 * @since 2016年5月11日15:31:39
 */
@SuppressWarnings("serial")
public class BaseEntity implements Serializable {
	private String id; // 主键ID UUid
	private String createDate; // 创建时间

	/**
	 * 生成UUID
	 * 
	 * @description
	 * @author yyj
	 * @create 2016年7月25日 下午3:07:27
	 * @return
	 */
	public String generateId() {
		if (StringUtils.isEmpty(id)) {
			id = UUID.randomUUID().toString().replaceAll("-", "");
		}
		return id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}