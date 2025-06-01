package com.jonas.backend.services;

import com.jonas.backend.entities.Livro;
import com.jonas.backend.entities.Compra;
import com.jonas.backend.entities.Venda;
import com.jonas.backend.services.exceptions.DatabaseException;
import com.jonas.backend.services.exceptions.ResourceNotFoundException;
import com.jonas.backend.repositories.LivroRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public Livro findById(Long id) {
        Optional<Livro> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Livro insert(Livro obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro de violação de "
                    + "integridade nos dados");
        }
    }

    public Livro update(Long id, Livro obj) {
        try {
            Livro entity = findById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);

        }

    }

    private void updateData(Livro entity, Livro obj) {
        entity.setTitulo(obj.getTitulo());
        entity.setAutor(obj.getAutor());
        entity.setEditora(obj.getEditora());
        entity.setLinkImg(obj.getLinkImg());
        entity.setAnoPublicacao(obj.getAnoPublicacao());

    }

    public Livro addBook(Long id, int quantityToAdd) {
        Livro livroToAdd = findById(id);
        livroToAdd.setEstoque(livroToAdd.getEstoque() + quantityToAdd);
        return repository.save(livroToAdd);
    }

    public Livro removeBook(Long id, int quantityToRemove) {
        Livro livroToRemove = findById(id);
        livroToRemove.setEstoque(livroToRemove.getEstoque() - quantityToRemove);
        return repository.save(livroToRemove);
    }

    public Livro updatePurchase(Long id, int quantity, Compra compra) {
        Livro livro = findById(id);

        int currentStock;

        if (quantity < compra.getQtdItens()) {
            currentStock = compra.getQtdItens() - quantity;
            livro.setEstoque(livro.getEstoque() - currentStock);
        } else if (quantity > compra.getQtdItens()) {
            currentStock = quantity - compra.getQtdItens();
            livro.setEstoque(livro.getEstoque() + currentStock);
        } else {
            livro.setEstoque(livro.getEstoque());
        }

        return repository.save(livro);
    }

    public Livro updateSale(Long id, int quantity, Venda venda) {
        Livro livroToAdd = findById(id);

        Integer currentStock;

        if (quantity < venda.getQtdItens()) {
            currentStock = venda.getQtdItens() - quantity;
            livroToAdd.setEstoque(livroToAdd.getEstoque() + currentStock);
        } else if (quantity > venda.getQtdItens()) {
            currentStock = quantity - venda.getQtdItens();
            livroToAdd.setEstoque(livroToAdd.getEstoque() - currentStock);
        } else {
            livroToAdd.setEstoque(livroToAdd.getEstoque());
        }

        return repository.save(livroToAdd);
    }

}
