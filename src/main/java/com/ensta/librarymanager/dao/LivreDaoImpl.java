package com.ensta.librarymanager.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ensta.librarymanager.exception.DaoException;
import com.ensta.librarymanager.modele.Livre;
import com.ensta.librarymanager.persistence.ConnectionManager;

public class LivreDaoImpl implements LivreDao {

    private static LivreDaoImpl instance;

    public static LivreDaoImpl getInstance() throws DaoException {
        if (instance == null) {
            instance = new LivreDaoImpl();
        }
        return instance;
    }

    private final String FIND_ALL = "SELECT id, titre, auteur, isbn, FROM livre";
    private final String FIND_BY_ID = "SELECT id, titre, auteur, isbn FROM livre WHERE id = ?";
    private final String CREATE = "INSERT INTO livre(titre, auteur, isbn) VALUES (?,?,?)";
    private final String UPDATE = "UPDATE livre SET titre = ?, auteur = ?, isbn = ? WHERE id = ?";
    private final String DELETE = "DELETE FROM livre WHERE id = ?";
    private final String COUNT = "SELECT COUNT(id) AS count FROM livre";

    private Connection connexion;
    
    private LivreDaoImpl() throws DaoException {
        try {
            connexion = ConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

	@Override
	public List<Livre> getList() throws DaoException {
        List<Livre> livres = new ArrayList<Livre>();
        try (PreparedStatement statement = connexion.prepareStatement(FIND_ALL);) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                livres.add(rowToLivre(resultSet));
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }

		return livres;
	}

	@Override
	public Livre getById(int id) throws DaoException {
        Livre livre = null;
        try (PreparedStatement statement = connexion.prepareStatement(FIND_BY_ID);) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                livre = rowToLivre(resultSet);
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DaoException();
        }
        return livre;
	}

	@Override
	public int create(String titre, String auteur, String isbn) throws DaoException {
        int id = -1;
        try (PreparedStatement statement = connexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, titre);
            statement.setString(2, auteur);
            statement.setString(3, isbn);

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
    public void update(Livre livre) throws DaoException {
        try (PreparedStatement statement = connexion.prepareStatement(UPDATE);) {
            statement.setString(1, livre.getTitre());
            statement.setString(2, livre.getAuteur());
            statement.setString(3, livre.getIsbn());
            statement.setInt(4, livre.getId());

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

    private Livre rowToLivre(ResultSet row) throws SQLException {
        Livre livre = new Livre();
        livre.setId(row.getInt("id"));
        livre.setTitre(row.getString("titre"));
        livre.setAuteur(row.getString("auteur"));
        livre.setIsbn(row.getString("isbn"));

        return livre;
    }
}
