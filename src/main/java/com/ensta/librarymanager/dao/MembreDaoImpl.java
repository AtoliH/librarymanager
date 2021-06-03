package com.ensta.librarymanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.modele.Membre;
import com.ensta.librarymanager.persistence.ConnectionManager;

public class MembreDaoImpl implements MembreDao {

    private static MembreDaoImpl instance;

    public static MembreDaoImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new MembreDaoImpl();
        }
        return instance;
    }

    private final String FIND_ALL = "SELECT * FROM membre";
    private final String FIND_BY_ID = "SELECT * FROM membre WHERE id = ?";
    private final String CREATE = "INSERT INTO membre(nom, prenom, adresse, email, telephone, abonnement) VALUES (?,?,?,?,?,?)";
    private final String UPDATE = "UPDATE membre SET nom = ?, prenom = ?, adresse = ?, email = ?, telephone = ?, abonnement = ? WHERE id = ?";
    private final String DELETE = "DELETE FROM membre WHERE id = ?";
    private final String COUNT = "SELECT COUNT(id) AS count FROM membre";

    private Connection connexion;
    
    public MembreDaoImpl() throws SQLException {
        connexion = ConnectionManager.getConnection();
    }

	@Override
	public List<Membre> getList() throws DaoException {
        List<Membre> membres = new ArrayList<Membre>();
        try (PreparedStatement statement = connexion.prepareStatement(FIND_ALL);) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                membres.add(rowToMember(resultSet));
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

		return membres;
	}

	@Override
	public Membre getById(int id) throws DaoException {
        Membre membre = null;
        try (PreparedStatement statement = connexion.prepareStatement(FIND_BY_ID);) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                membre = rowToMember(resultSet);
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
		return membre;
	}

	@Override
	public int create(String nom, String prenom, String adresse, String email, String telephone) throws DaoException {
        int id = -1;
        try (PreparedStatement statement = connexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, adresse);
            statement.setString(4, email);
            statement.setString(5, telephone);
            statement.setString(6, Membre.Abonnement.BASIC.name()); // On donne un abonnement BASIC par défaut
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
		return id;
	}

	@Override
	public void update(Membre membre) throws DaoException {
        try (PreparedStatement statement = connexion.prepareStatement(UPDATE);) {
            statement.setString(1, membre.getNom());
            statement.setString(2, membre.getPrenom());
            statement.setString(3, membre.getAdresse());
            statement.setString(4, membre.getEmail());
            statement.setString(5, membre.getTelephone());
            statement.setString(6, membre.getAbonnement().name());
            statement.setInt(7, membre.getId());

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
	}

	@Override
	public void delete(int id) throws DaoException {
		try (PreparedStatement statement = connexion.prepareStatement(DELETE);) {
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
	}

	@Override
	public int count() throws DaoException {
        int count = -1;
        try (PreparedStatement statement = connexion.prepareStatement(COUNT);) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

		return count;
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
