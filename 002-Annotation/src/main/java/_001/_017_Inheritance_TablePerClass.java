/*
1. Child class tables has all the columns related to parent's property, including parent's generatedValue. So each child has an Id as well generated by 
hibernate.
2. You dont have a discriminator here. Infact you don't need it since you have separate table for separate class
3. The tables are slighly more normalized. Unlike single class strategy, you don't have extra columns which do not have data. But this strategy repeats 
parent's columns in every child table.

drop table VEHICLE;
drop table FOUR_WHEELER;
drop table TWO_WHEELER;

CREATE TABLE VEHICLE (
  ID INT NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(15) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE FOUR_WHEELER (
  ID INT NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(15) NOT NULL,
  STEERINGWHEEL VARCHAR(15),
  PRIMARY KEY (ID)
);

CREATE TABLE TWO_WHEELER (
  ID INT NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(15) NOT NULL,
  HANDLE VARCHAR(15),
  PRIMARY KEY (ID)
);

select * from VEHICLE;
select * from FOUR_WHEELER;
select * from TWO_WHEELER;

*/

package _001;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

@Entity
@Table(name="VEHICLE")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
class _017Vehicle {
	
	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
	int vehicleId;
	
	@Column(name = "NAME")
	String vehicleName;

	public _017Vehicle() {
		super();
	}

	public _017Vehicle(String vehicleName) {
		super();
		this.vehicleName = vehicleName;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	@Override
	public String toString() {
		return "_017Vehicle [vehicleId=" + vehicleId + ", vehicleName=" + vehicleName + "]";
	}
}

@Entity
@Table(name="FOUR_WHEELER")
class _017FourWheeler extends _017Vehicle {	
	
	@Column(name = "STEERINGWHEEL")
	private String steeringWheel;

	public _017FourWheeler() {
		super();
	}

	public _017FourWheeler(String steeringWheel) {
		super();
		this.steeringWheel = steeringWheel;
	}

	public String getSteeringWheel() {
		return steeringWheel;
	}

	public void setSteeringWheel(String steeringWheel) {
		this.steeringWheel = steeringWheel;
	}
}

@Entity
@Table(name="TWO_WHEELER")
class _017TwoWheeler extends _017Vehicle {
	
	@Column(name = "HANDLE")
	private String handle;

	public _017TwoWheeler() {
		super();
	}

	public _017TwoWheeler(String handle) {
		super();
		this.handle = handle;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}	
}

public class _017_Inheritance_TablePerClass{
	public static void main(String[] args) throws IOException {
		Configuration cfg = new Configuration().configure("001/017.hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		_017_Inheritance_TablePerClass demo = new _017_Inheritance_TablePerClass();
		demo.insert(sf);
	}
	
	public void insert(SessionFactory sf ){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			_017Vehicle vehicle1 = new _017Vehicle("gaadi");
			_017FourWheeler vehicle2 = new _017FourWheeler("civic"); vehicle2.setVehicleName("honda");
			_017TwoWheeler vehicle3 = new _017TwoWheeler("activa"); vehicle3.setVehicleName("kinetic");
			session.save(vehicle1);
			session.save(vehicle2);
			session.save(vehicle3);
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		}finally {
			session.close(); 
		}
	}
	/*
	OUTPUT:
	Hibernate: select tbl.next_val from hibernate_sequences tbl where tbl.sequence_name=? for update
	Hibernate: update hibernate_sequences set next_val=?  where next_val=? and sequence_name=?
	Hibernate: select tbl.next_val from hibernate_sequences tbl where tbl.sequence_name=? for update
	Hibernate: update hibernate_sequences set next_val=?  where next_val=? and sequence_name=?
	Hibernate: select tbl.next_val from hibernate_sequences tbl where tbl.sequence_name=? for update
	Hibernate: update hibernate_sequences set next_val=?  where next_val=? and sequence_name=?
	Hibernate: insert into VEHICLE (NAME, ID) values (?, ?)
	Hibernate: insert into FOUR_WHEELER (NAME, STEERINGWHEEL, ID) values (?, ?, ?)
	Hibernate: insert into TWO_WHEELER (NAME, HANDLE, ID) values (?, ?, ?)
	 
	 ID          NAME            
	----------- --------------- 
	3           gaadi                     
	
	
	ID          NAME            STEERINGWHEEL   
	----------- --------------- --------------- 
	4           honda           civic                     
	
	
	ID          NAME            HANDLE          
	----------- --------------- --------------- 
	5           kinetic         activa           
	 
	 */
}
