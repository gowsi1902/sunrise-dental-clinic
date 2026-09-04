package com.sunrise.service;

import java.util.List;

import com.sunrise.dao.DentistDao;
import com.sunrise.dao.TreatmentDao;
import com.sunrise.model.Dentist;
import com.sunrise.model.Treatment;

public class CatalogService {
    private final DentistDao dentistDao = new DentistDao();
    private final TreatmentDao treatmentDao = new TreatmentDao();

    public List<Dentist> dentists() {
        return dentistDao.findAll();
    }

    public List<Treatment> treatments() {
        return treatmentDao.findAll();
    }
}
