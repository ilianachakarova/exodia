package chakarova.exodia.service;

import chakarova.exodia.domain.model.service.DocumentServiceModel;

import java.util.List;

public interface DocumentService {
    DocumentServiceModel scheduleDocument(DocumentServiceModel documentServiceModel);

    DocumentServiceModel findDocumentById(String id);

    List<DocumentServiceModel> findAllDocuments();

   boolean printDocumentById(String id);
}
