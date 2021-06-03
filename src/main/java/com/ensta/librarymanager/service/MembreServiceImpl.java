package com.ensta.librarymanager.service;

import java.util.function.Function;

import java.util.List;
import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.exception.ServiceException;
import com.ensta.librarymanager.dao.MembreDao;
import com.ensta.librarymanager.dao.MembreDaoImpl;
import com.ensta.librarymanager.modele.Membre;

public class MembreServiceImpl implements MembreService {

    private static MembreServiceImpl instance;

    public static MembreServiceImpl getInstance() throws ServiceException {
        if (instance == null) {
            instance = new MembreServiceImpl();
        }
        return instance;
    }

    private MembreDao dao;

    private MembreServiceImpl() throws ServiceException {
        try {
            dao = MembreDaoImpl.getInstance();
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
	public List<Membre> getList() throws ServiceException {
        List<Membre> membres = null;

        try {
            membres = dao.getList();
        } catch(DaoException e) {
            throw new ServiceException();
        }

        return membres;
    }

    @Override
	public List<Membre> getListMembreEmpruntPossible() throws ServiceException {
        List<Membre> membres = getList();

        EmpruntService empruntService = EmpruntServiceImpl.getInstance();

        membres.removeIf((m) -> {
            boolean ret = false;
            try {
                ret = !empruntService.isEmpruntPossible(m);
            } catch (ServiceException e) {}
            return ret;
        });

        return membres;
    }

    @Override
	public Membre getById(int id) throws ServiceException {
        Membre membre = null;

        try {
            membre = dao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException();
        }
        
        return membre;
    }

    @Override
	public int create(String nom, String prenom, String adresse, String email, String telephone) throws ServiceException {
        int id = -1;

        if (prenom.isEmpty() || nom.isEmpty())
            throw new ServiceException();

        try {
            id = dao.create(nom.toUpperCase(), prenom.toUpperCase(), adresse, email, telephone);
        } catch (DaoException e) {
            throw new ServiceException();
        }

        return id;
    }

    @Override
	public void update(Membre membre) throws ServiceException {
        try {
            dao.update(membre);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
	public void delete(int id) throws ServiceException {
        try {
            dao.delete(id);
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
}
