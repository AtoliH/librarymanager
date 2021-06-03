package com.ensta.librarymanager.dao;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;

import com.ensta.librarymanager.persistence.ConnectionManager;

import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.modele.Emprunt;
import com.ensta.librarymanager.modele.Membre;
import com.ensta.librarymanager.modele.Livre;
import com.ensta.librarymanager.dao.MembreDao;
import com.ensta.librarymanager.dao.MembreDaoImpl;
import com.ensta.librarymanager.dao.LivreDao;
import com.ensta.librarymanager.dao.LivreDaoImpl;

public class EmpruntDaoImpl implements EmpruntDao {

    private static EmpruntDaoImpl instance;

    public static EmpruntDaoImpl getInstance() throws DaoException {
        if (instance == null) {
            instance = new EmpruntDaoImpl();
        }
        return instance;
    }

    private final String FIND_ALL = "SELECT e.id AS id, idMembre, nom, prenom, adresse, email, telephone, abonnement, idEmprunt, titre, auteur, isbn, dateEmprunt, dateRetour FROM emprunt AS e INNER JOIN membre ON membre.id = e.idMembre INNER JOIN emprunt ON emprunt.id = e.idEmprunt ORDER BY dateRetour DESC";
    private final String FIND_CURRENT = "SELECT e.id AS id, idMembre, nom, prenom, adresse, email, telephone, abonnement, idLivre, titre, auteur, isbn, dateEmprunt, dateRetour FROM emprunt AS e INNER JOIN membre ON membre.id = e.idMembre INNER JOIN emprunt ON emprunt.id = e.idEmprunt WHERE dateRetour IS NULL";
    private final String FIND_CURRENT_BY_MEMBRE = "SELECT e.id AS id, idMembre, nom, prenom, adresse, email, telephone, abonnement, idEmprunt, titre, auteur, isbn, dateEmprunt, dateRetour FROM emprunt AS e INNER JOIn membre ON membre.id = e.idMembre INNER JOIN emprunt ON emprunt.id = e.idEmprunt WHERE dateRetour IS NULL AND membre.id = ?";
    private final String FIND_CURRENT_BY_LIVRE = "SELECT e.id AS id, idMembre, nom, prenom, adresse, email, telephone, abonnement, idLivre, titre, auteur, isbn, dateEmprunt, dateRetour FROM emprunt AS e INNER JOIN membre ON membre.id = e.idMembre INNER JOIN emprunt ON emprunt.id = e.idEmprunt WHERE dateRetour IS NULL AND emprunt.id = ?";
    private final String FIND_BY_ID = "SELECT e.id AS idEmprunt, idMembre, nom, prenom, adresse, email, telephone, abonnement, idEmprunt, titre, auteur, isbn, dateEmprunt, dateRetour FROM emprunt as e INNER JOIN membre ON membre.id = e.idMembre INNER JOIN emprunt ON emprunt.id = e.idEmprunt WHERE e.id = ?";
    private final String CREATE = "INSERT INTO emprunt(idMembre, idEmprunt, dateEmprunt, dateRetour) VALUES(?,?,?,?)";
    private final String UPDATE = "UPDATE emprunt SET idMembre = ?, idEmprunt = ?, dateEmprunt = ?, dateRetour = ? WHERE id = ?";
    private final String COUNT = "SELECT COUNT(id) AS count FROM emprunt";

    private Connection connexion;

    private EmpruntDaoImpl() throws DaoException {
        try {
            connexion = ConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    @Override
    public List<Emprunt> getList() throws DaoException {
        List<Emprunt> emprunts = new ArrayList<Emprunt>();
        try (PreparedStatement statement = connexion.prepareStatement(FIND_ALL);) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                emprunts.add(rowToEmprunt(resultSet));
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

        return emprunts;
    }

    @Override
    public List<Emprunt> getListCurrent() throws DaoException {
        List<Emprunt> emprunts = new ArrayList<Emprunt>();
        try (PreparedStatement statement = connexion.prepareStatement(FIND_CURRENT);) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                emprunts.add(rowToEmprunt(resultSet));
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

        return emprunts;
    }

    @Override
    public List<Emprunt> getListCurrentByMembre(int idMembre) throws DaoException {
        List<Emprunt> emprunts = new ArrayList<Emprunt>();
        try (PreparedStatement statement = connexion.prepareStatement(FIND_CURRENT_BY_MEMBRE);) {
            statement.setInt(1, idMembre);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                emprunts.add(rowToEmprunt(resultSet));
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

        return emprunts;
    }

    @Override
    public List<Emprunt> getListCurrentByLivre(int idLivre) throws DaoException {
        List<Emprunt> emprunts = new ArrayList<Emprunt>();
        try (PreparedStatement statement = connexion.prepareStatement(FIND_CURRENT_BY_LIVRE);) {
            statement.setInt(1, idLivre);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                emprunts.add(rowToEmprunt(resultSet));
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

        return emprunts;
    }

    @Override
    public Emprunt getById(int id) throws DaoException {
        Emprunt emprunt = null;
        try (PreparedStatement statement = connexion.prepareStatement(FIND_BY_ID);) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                emprunt = rowToEmprunt(resultSet);
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
        return emprunt;

    }

    @Override
    public void create(int idMembre, int idEmprunt, LocalDate dateEmprunt) throws DaoException {
        try (PreparedStatement statement = connexion.prepareStatement(CREATE);) {
            statement.setInt(1, idMembre);
            statement.setInt(2, idEmprunt);
            statement.setDate(3, Date.valueOf(dateEmprunt));
            statement.setDate(4, null);

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
    }

    @Override
    public void update(Emprunt emprunt) throws DaoException {
        try (PreparedStatement statement = connexion.prepareStatement(UPDATE);) {
            statement.setInt(1, emprunt.getMembre().getId());
            statement.setInt(2, emprunt.getLivre().getId());
            statement.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            statement.setDate(4, Date.valueOf(emprunt.getDateRetour()));
            statement.setInt(5, emprunt.getId());

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

    private Emprunt rowToEmprunt(ResultSet row) throws SQLException, DaoException {
        Emprunt emprunt = new Emprunt();
        emprunt.setId(row.getInt("id"));
        emprunt.setMembre(MembreDaoImpl.getInstance().getById(row.getInt("idMembre")));
        emprunt.setLivre(LivreDaoImpl.getInstance().getById(row.getInt("idLivre")));
        emprunt.setDateEmprunt(row.getDate("dateEmprunt").toLocalDate());
        emprunt.setDateRetour(row.getDate("dateRetour").toLocalDate());

        return emprunt;
    }

}
