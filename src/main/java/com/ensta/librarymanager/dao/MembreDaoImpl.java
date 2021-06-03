package com.ensta.librarymanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.modele.Membre;
import com.ensta.librarymanager.persistence.ConnectionManager;

public class MembreDaoImpl implements MembreDao {

    private Connection connexion;

    private final String FIND_ALL = "SELECT * FROM membre";
    private final String FIND_ID = "SELECT * FROM membre WHERE id = ?";
    
    public MembreDaoImpl() throws SQLException {
        connexion = ConnectionManager.getConnection();
    }

	@Override
	public List<Membre> getList() throws DaoException {
        List<Membre> membres = new ArrayList<Membre>();
        try (PreparedStatement statement = connexion.prepareStatement(FIND_ALL);) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                membres.add(rowToMember(result));
            }

            result.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

		return membres;
	}

	@Override
	public Membre getById(int id) throws DaoException {
        Membre membre = null;
        try (PreparedStatement statement = connexion.prepareStatement(FIND_ID);) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                membre = rowToMember(result);
            }

            result.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
		return membre;
	}

	@Override
	public int create(String nom, String prenom, String adresse, String email, String telephone) throws DaoException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(Membre membre) throws DaoException {
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

    private Membre rowToMember(ResultSet row) throws SQLException {
        Membre membre = new Membre();
        membre.setId(row.getInt("id"));
        membre.setNom(row.getString("nom"));
        membre.setPrenom(row.getString("prenom"));
        membre.setAdresse(row.getString("adresse"));
        membre.setEmail(row.getString("email"));
        membre.setTelephone(row.getString("telephone"));
        membre.setAbonnement(Membre.Abonnement.valueOf(row.getString("abonnement")));

        return membre;
    }
}
