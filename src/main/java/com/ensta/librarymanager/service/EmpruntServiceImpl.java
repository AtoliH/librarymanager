package com.ensta.librarymanager.service;

import java.time.LocalDate;
import java.util.List;

import com.ensta.librarymanager.modele.Emprunt;
import com.ensta.librarymanager.modele.Membre;

public class EmpruntServiceImpl implements EmpruntService {

	@Override
	public List<Emprunt> getList() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emprunt> getListCurrent() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emprunt> getListCurrentByMembre(int idMembre) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Emprunt> getListCurrentByLivre(int idLivre) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Emprunt getById(int id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(int idMembre, int idLivre, LocalDate dateEmprunt) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnBook(int id) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int count() throws ServiceException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isLivreDispo(int idLivre) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpruntPossible(Membre membre) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}
}
