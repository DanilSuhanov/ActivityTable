package ru.suhanov.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.suhanov.model.request.ImpRequest;

public interface ImpRequestRepository extends JpaRepository<ImpRequest, Long> {
    ImpRequest findImpRequestById(long id);
}
