package ru.suhanov.service.interfaces;

import ru.suhanov.model.request.ImpRequest;

public interface ImpRequestService {
    void addNewImpRequest(ImpRequest impRequest);
    void deleteImpRequestById(long id);
    ImpRequest findRequestById(long id);
}
