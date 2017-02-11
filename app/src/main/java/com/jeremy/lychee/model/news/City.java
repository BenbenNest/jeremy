
package com.jeremy.lychee.model.news;

import android.text.TextUtils;

import java.io.Serializable;

public class City implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    /**
     * 地级市
     */
    private String city;
    /**
     * 区/县
     */
    private String district;
    /**
     * 省份
     */
    private String province;
    /**
     * 区code
     */
    private String code;
    /**
     * 城市拼音
     */
    private String citySpell;
    /**
     * 省份拼音
     */
    private String provinceSpell;
    /**
     * 是否为自动获得的位置
     */
    private boolean isAuto;

    public City() {
        this.city = "";
        this.district = "";
        this.province = "";
        this.code = "";
        this.citySpell = "";
        this.provinceSpell = "";
        this.isAuto = false;
    }

    public void merge(City city) {
        if (city == null)
            return;
        this.city = city.getCity();
        this.district = city.getDistrict();
        this.province = city.getProvince();
        this.code = city.getCode();
        this.citySpell = city.getCitySpell();
        this.provinceSpell = city.getProvinceSpell();
        this.isAuto = city.isAuto();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public String getCitySpell() {
        return citySpell;
    }

    public void setCitySpell(String spell) {
        this.citySpell = spell;
    }

    public String getProvinceSpell() {
        return provinceSpell;
    }

    public void setProvinceSpell(String provinceSpell) {
        this.provinceSpell = provinceSpell;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        String name = district;
        if (TextUtils.isEmpty(name)) {
            name = city;
        }
        if (TextUtils.isEmpty(name)) {
            name = province;
        }
        return name;
    }

}
