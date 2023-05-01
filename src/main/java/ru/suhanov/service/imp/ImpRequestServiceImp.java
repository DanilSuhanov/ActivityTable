package ru.suhanov.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.suhanov.model.request.ImpRequest;
import ru.suhanov.repositoty.ImpRequestRepository;
import ru.suhanov.service.interfaces.ImpRequestService;

import javax.transaction.Transactional;

@Service
@Transactional
public class ImpRequestServiceImp implements ImpRequestService {

    private final ImpRequestRepository impRequestRepository;

    @Autowired
    public ImpRequestServiceImp(ImpRequestRepository impRequestRepository) {
        this.impRequestRepository = impRequestRepository;
    }

    @Override
    public void addNewImpRequest(ImpRequest impRequest) {
        impRequestRepository.save(impRequest);
    }

    @Override
    public void deleteImpRequestById(long id) {
        ImpRequest impRequest = impRequestRepository.findImpRequestById(id);
        if (impRequest != null) {
            impRequestRepository.delete(impRequest);
        }
    }

    @Override
    public ImpRequest findRequestById(long id) {
        return impRequestRepository.findImpRequestById(id);
    }
}
