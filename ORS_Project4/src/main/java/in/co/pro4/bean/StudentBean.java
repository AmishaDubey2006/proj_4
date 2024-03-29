package in.co.pro4.bean;

import java.util.Date;

/**
 * Student JavaBean encapsulates Student attributes
 * @author Amisha Dubey
 *
 */
public class StudentBean extends BaseBean {
	
	/**
     * First Name of Student
     */
	private String firstName;
	
    /**
     * Last Name of Student
     */
	private String lastName;
	
	/**
     * Date of Birth of Student
     */
	private Date dob;
	
	/**
     * Mobile No of Student
     */
	private String mobileNo;
	
	/**
     * Email of Student
     */
	private String email;
	
	/**
     * CollegeId of Student
     */
	private long collegeId;
	
	/**
     * College name of Student
     */
	private String collegeName;
	
	
	/**
     * accessor
     */
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(long collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	@Override
	public String getKey() {

		return id + "";
	}

	@Override
	public String getValue() {
		return firstName + " " + lastName;
	}

}
