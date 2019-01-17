package org.isf.admission.service;

import java.util.GregorianCalendar;
import java.util.List;

import org.isf.admission.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AdmissionIoOperationRepository extends JpaRepository<Admission, Integer>, AdmissionIoOperationRepositoryCustom {    
    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_IN = 1 AND ADM_WRD_ID_A = :ward", nativeQuery= true)
    public List<Admission> findAllWhereWard(@Param("ward") String ward);
    
    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_PAT_ID=:patient AND ADM_DELETED='N' AND ADM_DATE_DIS IS NULL", nativeQuery= true)
    public List<Admission> findAllWherePatient(@Param("patient") Integer patient);

    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_PAT_ID=? and ADM_DELETED='N' ORDER BY ADM_DATE_ADM ASC", nativeQuery= true)
    public List<Admission> findAllWherePatientByOrderByDate(@Param("patient") Integer patient);
        
    @Query(value = "SELECT * FROM ADMISSION " +
    		"WHERE ADM_WRD_ID_A=:ward AND ADM_DATE_ADM >= :dateFrom AND ADM_DATE_ADM <= :dateTo AND ADM_DELETED='N' " +
    		"ORDER BY ADM_YPROG DESC", nativeQuery= true)
    public List<Admission> findAllWhereWardAndDates(
    		@Param("ward") String ward, @Param("dateFrom") GregorianCalendar dateFrom,
    		@Param("dateTo") GregorianCalendar dateTo);
    
    @Query(value = "SELECT * FROM ADMISSION WHERE ADM_IN = 1 AND ADM_WRD_ID_A = :ward AND ADM_DELETED = 'N'", nativeQuery= true)
    public List<Admission> findAllWhereIn1(@Param("ward") String ward);   
    
}