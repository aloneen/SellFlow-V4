package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.entities.SupportMessage;
import kz.seisen.sellflowv4.repositories.SupportMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportMessageService {

    @Autowired
    private SupportMessageRepository supportMessageRepository;

    public void save(SupportMessage message) {
        supportMessageRepository.save(message);
    }

    public List<SupportMessage> getAllMessages() {
        return supportMessageRepository.findAll();
    }
}
