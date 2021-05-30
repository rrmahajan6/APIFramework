package forPracticePurposeONLY;

import java.util.List;

public class ApiData {
	private String accuracy;
	private String status;
	private String name;
	private String phone_number;
	private String address;
	private String website;
	private String language;
	private Location location;
	private List<String> types;
	private String place_id;
	private String scope;
	private String reference;
	private String id;
//	public String getId() {
//		return id;
//	}
	public void setId(String id) {
		this.id = id;
	}
//	public String getReference() {
//		return reference;
//	}
	public void setReference(String reference) {
		this.reference = reference;
	}
//	public String getScope() {
//		return scope;
//	}
	public void setScope(String scope) {
		this.scope = scope;
	}
//	public String getPlace_id() {
//		return place_id;
//	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
}
