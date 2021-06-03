package com.ensta.librarymanager.dao;

import java.time.LocalDate;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import com.ensta.librarymanager.persistence.ConnectionManager;

import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.modele.Emprunt;

public class EmpruntDaoImpl implements EmpruntDao {

    private Connection connexion;
    
    public EmpruntDaoImpl() throws SQLException {
        connexion = ConnectionManager.getConnection();
    }

	@Override
	public List<Emprunt> getList() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emprunt> getListCurrent() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emprunt> getListCurrentByMembre(int idMembre) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emprunt> getListCurrentByLivre(int idLivre) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Emprunt getById(int id) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(int idMembre, int idLivre, LocalDate dateEmprunt) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Emprunt emprunt) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int count() throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}
}
