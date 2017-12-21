package css.com.xsp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the S_DICT_ITEM database table.
 * 
 */
@Entity
@Table(name="S_DICT_ITEM")
@NamedQuery(name="SDictItem.findAll", query="SELECT s FROM SDictItem s")
public class SDictItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_ID")
	private String uuid;
	
	@Column(name="DICT_CODE")
	private String code;

	@Column(name="ITEM_CODE")
	private String itemCode;

	@Column(name="ITEM_NAME")
	private String itemName;

	@Column(name="ITEM_ORDER")
	private Integer itemOrder;

	private String remark;

	@Column(name="VALID_MARK")
	private String validMark;

	public SDictItem() {
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemOrder() {
		return this.itemOrder;
	}

	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getValidMark() {
		return this.validMark;
	}

	public void setValidMark(String validMark) {
		this.validMark = validMark;
	}

}








