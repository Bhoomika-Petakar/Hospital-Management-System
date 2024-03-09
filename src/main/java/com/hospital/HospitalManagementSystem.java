package com.hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
	private static final String url="jdbc:mysql://localhost:3306/hospital";
			private static final String username="root";
			private static final String password="Bhoomi@007";
			
			public static void main(String[] args) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				}catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
				Scanner scan=new Scanner(System.in);
				try {
					
					Connection connection=DriverManager.getConnection(url,username,password);
					Patient patient=new Patient(connection, scan);
					Doctors doctor=new Doctors(connection);
					while(true) {
						System.out.println("Hospital Management System");
						System.out.println("1.add Patient");
						System.out.println("2.View patient");
						System.out.println("3.View Doctors");
						System.out.println("4.Book Appointment");
						System.out.println("5.Exit");
						System.out.println();
						System.out.println("Enter your choice: ");
						int choice=scan.nextInt();
						
						switch (choice) {
						case 1: 
							patient.addpatient();
							System.out.println();
							break;
						case 2:
							patient.viewpatients();
							System.out.println();
							break;
						case 3:
							doctor.viewDoctors();
							System.out.println();
							break;
						case 4:
							bookappointment(patient,doctor,connection,scan);
							System.out.println();
							break;

						
						case 5:
							System.out.println("!---Thank you for using Hospital managment system---!");

							return;
							
							
						
						
						default:
							System.out.println("Enter valid choice !!!");
						}

					}
					
					
					
					
					

				}catch(SQLException e) {
					e.printStackTrace();
				}
			}

	public static void bookappointment(Patient patient,Doctors doctor,Connection connection,Scanner scan) {
		System.out.println("Enter Patient id: ");
		int patientId=scan.nextInt();
		System.out.println("Enter doctor id: ");
		int doctorId=scan.nextInt();
		System.out.println("Enter appointment date(YYYY-MM-DD): ");
		String appointmentDate=scan.next();
		if(patient.getPatientById(patientId) && doctor.getdoctorById(doctorId)) {
			if(checkDoctorAvailibility(doctorId,appointmentDate,connection)) {
				String appointmentQuery="INSERT INTO appointments(patient_id,doctor_id,appointment_date)values(?,?,?)";
				try {
					PreparedStatement preparedStatement=connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1, patientId);
					preparedStatement.setInt(2, doctorId);
					preparedStatement.setString(3, appointmentDate);
					int Rowsaffected=preparedStatement.executeUpdate();

					if(Rowsaffected>0) {
						System.out.println("Appointment Booked..");
					}
					else {
						System.out.println("Failed to Book Appointment!!!");
					}

				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Doctor is not available on this date!!");
			}
		}else {
			System.out.println("Either doctor or patient doesnt exist!!!");
		}
	
	}
public static boolean checkDoctorAvailibility(int doctorId,String appointmentDate,Connection connection) {
	String query="SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date=?";

try {
	PreparedStatement preparedStatement=connection.prepareStatement(query);
	preparedStatement.setInt(1, doctorId);
	preparedStatement.setString(2, appointmentDate);
	ResultSet resultset=preparedStatement.executeQuery();
	if(resultset.next()) {
		int count=resultset.getInt(1);
		if(count==0) {
			return true;
		}else {
			return false;
		}
	}

	

	
} catch (Exception e) {
e.printStackTrace();}
	
	
	return false;
	
}
}
