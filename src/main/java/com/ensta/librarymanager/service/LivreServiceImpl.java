package com.ensta.librarymanager.service;

import java.util.List;
import java.util.function.Predicate;

import com.ensta.librarymanager.exception.ServiceException;
import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.dao.LivreDao;
import com.ensta.librarymanager.dao.LivreDaoImpl;
import com.ensta.librarymanager.modele.Livre;
import com.ensta.librarymanager.service.EmpruntServiceImpl;
import com.ensta.librarymanager.service.EmpruntService;

public class LivreServiceImpl implements LivreService {

    private static LivreServiceImpl instance;

    public static LivreServiceImpl getInstance() throws ServiceException {
        if (instance == null) {
            instance = new LivreServiceImpl();
        }
        return instance;
    }

    private LivreDao dao;

    private LivreServiceImpl() throws ServiceException {
        try {
            dao = LivreDaoImpl.getInstance();
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    @Override
    public List<Livre> getList() throws ServiceException {
        List<Livre> livres = null;

        try {
            livres = dao.getList();
        } catch(DaoException e) {
            throw new ServiceException();
        }

        return livres;
    }

    @Override
    public List<Livre> getListDispo() throws ServiceException {
        final EmpruntService empruntService = EmpruntServiceImpl.getInstance();

        List<Livre> livres = getList();

        livres.removeIf((l) -> {
            boolean ret = false;
            try {
                ret = !empruntService.isLivreDispo(l.getId());
            } catch (ServiceException e) {}
            return ret;
        });

        return livres;
    }

    @Override
    public Livre getById(int id) throws ServiceException {
        Livre livre = null;

        try {
            livre = dao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException();
        }

        return livre;

    }
    @Override
    public int create(String titre, String auteur, String isbn) throws ServiceException {
        int id = -1;

        try {
            id = dao.create(titre, auteur, isbn);
        } catch (DaoException e) {
            throw new ServiceException();
        }

        return id;

    }
    @Override
    public void update(Livre livre) throws ServiceException {
        try {
            dao.update(livre);
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
