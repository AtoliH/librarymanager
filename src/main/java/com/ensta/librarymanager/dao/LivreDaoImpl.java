package com.ensta.librarymanager.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.modele.Livre;
import com.ensta.librarymanager.persistence.ConnectionManager;

public class LivreDaoImpl implements LivreDao {

    private Connection connexion;
    
    public LivreDaoImpl() throws SQLException {
        connexion = ConnectionManager.getConnection();
    }

	@Override
	public List<Livre> getList() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Livre getById(int id) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int create(String titre, String auteur, String isbn) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(Livre livre) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int count() throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}
}
