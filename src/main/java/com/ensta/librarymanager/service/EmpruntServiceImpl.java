package com.ensta.librarymanager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.exception.ServiceException;
import com.ensta.librarymanager.modele.Emprunt;
import com.ensta.librarymanager.modele.Membre;
import com.ensta.librarymanager.dao.EmpruntDaoImpl;
import com.ensta.librarymanager.dao.EmpruntDao;
import com.ensta.librarymanager.service.EmpruntService;

public class EmpruntServiceImpl implements EmpruntService {

    private static EmpruntServiceImpl instance;

    public static EmpruntServiceImpl getInstance() throws ServiceException {
        if (instance == null) {
            instance = new EmpruntServiceImpl();
        }
        return instance;
    }

    private EmpruntDao dao;

    private EmpruntServiceImpl() throws ServiceException {
        try {
            dao = EmpruntDaoImpl.getInstance();
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
    public List<Emprunt> getList() throws ServiceException {
        List<Emprunt> emprunts = null;

        try {
            emprunts = dao.getList();
        } catch(DaoException e) {
            throw new ServiceException();
        }

        return emprunts;
    }

    @Override
    public List<Emprunt> getListCurrent() throws ServiceException {
        List<Emprunt> emprunts = null;

        try {
            emprunts = dao.getListCurrent();
        } catch(DaoException e) {
            throw new ServiceException();
        }

        return emprunts;
    }

    @Override
    public List<Emprunt> getListCurrentByMembre(int idMembre) throws ServiceException {
        List<Emprunt> emprunts = null;

        try {
            emprunts = dao.getListCurrentByMembre(idMembre);
        } catch(DaoException e) {
            throw new ServiceException();
        }

        return emprunts;
    }

    @Override
    public List<Emprunt> getListCurrentByLivre(int idLivre) throws ServiceException {
        List<Emprunt> emprunts = null;

        try {
            emprunts = dao.getListCurrentByLivre(idLivre);
        } catch(DaoException e) {
            throw new ServiceException();
        }

        return emprunts;
    }

    @Override
    public Emprunt getById(int id) throws ServiceException {
        Emprunt emprunt = null;

        try {
            emprunt = dao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException();
        }

        return emprunt;
    }

    @Override
    public void create(int idEmprunt, int idLivre, LocalDate dateEmprunt) throws ServiceException {
        try {
            dao.create(idEmprunt, idLivre, dateEmprunt);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
    public void returnBook(int id) throws ServiceException {
        Emprunt emprunt = getById(id);
        emprunt.setDateRetour(LocalDate.now());

        try {
            dao.update(emprunt);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
    public int count() throws ServiceException {
        int count = -1;

        try {
            dao.count();
        } catch (DaoException e) {
            throw new ServiceException();
        }

        return count;
    }

    @Override
    public boolean isLivreDispo(int idLivre) throws ServiceException {
        List<Emprunt> emprunts = getListCurrentByLivre(idLivre);
        return emprunts.isEmpty();
    }

    @Override
    public boolean isEmpruntPossible(Membre membre) throws ServiceException {
        // Un membre ne peut pas emprunter plus que ce que sont forfait lui permet
        List<Emprunt> emprunts = getListCurrentByMembre(membre.getId());

        Map<Membre.Abonnement, Integer> nombreEmpruntsMax = Map.ofEntries(
            Map.entry(Membre.Abonnement.BASIC, 2),
            Map.entry(Membre.Abonnement.PREMIUM, 5),
            Map.entry(Membre.Abonnement.VIP, 20)
        );

        return emprunts.size() < nombreEmpruntsMax.get(membre.getAbonnement());
    }
}
