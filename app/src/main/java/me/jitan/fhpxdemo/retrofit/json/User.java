package me.jitan.fhpxdemo.retrofit.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

  @Expose private Integer id;
  @Expose private String username;
  @Expose private String firstname;
  @Expose private String lastname;
  @Expose private String city;
  @Expose private String country;
  @Expose private Integer usertype;
  @Expose private String fullname;
  @SerializedName("userpic_url") @Expose private String userpicUrl;
  @SerializedName("userpic_https_url") @Expose private String userpicHttpsUrl;
  @SerializedName("cover_url") @Expose private String coverUrl;
  @SerializedName("upgrade_status") @Expose private Integer upgradeStatus;
  @SerializedName("store_on") @Expose private Boolean storeOn;
  @Expose private Integer affection;
  @SerializedName("followers_count") @Expose private Integer followersCount;

  /**
   * @return The id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id The id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return The username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username The username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return The firstname
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * @param firstname The firstname
   */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  /**
   * @return The lastname
   */
  public String getLastname() {
    return lastname;
  }

  /**
   * @param lastname The lastname
   */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  /**
   * @return The city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city The city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return The country
   */
  public String getCountry() {
    return country;
  }

  /**
   * @param country The country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * @return The usertype
   */
  public Integer getUsertype() {
    return usertype;
  }

  /**
   * @param usertype The usertype
   */
  public void setUsertype(Integer usertype) {
    this.usertype = usertype;
  }

  /**
   * @return The fullname
   */
  public String getFullname() {
    return fullname;
  }

  /**
   * @param fullname The fullname
   */
  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  /**
   * @return The userpicUrl
   */
  public String getUserpicUrl() {
    return userpicUrl;
  }

  /**
   * @param userpicUrl The userpic_url
   */
  public void setUserpicUrl(String userpicUrl) {
    this.userpicUrl = userpicUrl;
  }

  /**
   * @return The userpicHttpsUrl
   */
  public String getUserpicHttpsUrl() {
    return userpicHttpsUrl;
  }

  /**
   * @param userpicHttpsUrl The userpic_https_url
   */
  public void setUserpicHttpsUrl(String userpicHttpsUrl) {
    this.userpicHttpsUrl = userpicHttpsUrl;
  }

  /**
   * @return The coverUrl
   */
  public String getCoverUrl() {
    return coverUrl;
  }

  /**
   * @param coverUrl The cover_url
   */
  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }

  /**
   * @return The upgradeStatus
   */
  public Integer getUpgradeStatus() {
    return upgradeStatus;
  }

  /**
   * @param upgradeStatus The upgrade_status
   */
  public void setUpgradeStatus(Integer upgradeStatus) {
    this.upgradeStatus = upgradeStatus;
  }

  /**
   * @return The storeOn
   */
  public Boolean getStoreOn() {
    return storeOn;
  }

  /**
   * @param storeOn The store_on
   */
  public void setStoreOn(Boolean storeOn) {
    this.storeOn = storeOn;
  }

  /**
   * @return The affection
   */
  public Integer getAffection() {
    return affection;
  }

  /**
   * @param affection The affection
   */
  public void setAffection(Integer affection) {
    this.affection = affection;
  }

  /**
   * @return The followersCount
   */
  public Integer getFollowersCount() {
    return followersCount;
  }

  /**
   * @param followersCount The followers_count
   */
  public void setFollowersCount(Integer followersCount) {
    this.followersCount = followersCount;
  }
}
