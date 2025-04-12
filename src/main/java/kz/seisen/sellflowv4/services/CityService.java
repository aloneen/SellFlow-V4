package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.entities.City;
import kz.seisen.sellflowv4.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public void saveCity(City city) {
        cityRepository.save(city);
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public void deleteCityById(Long id) {
        cityRepository.deleteById(id);
    }
}
