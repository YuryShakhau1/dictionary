package by.shakhau.dictionary.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.shakhau.dictionary.logic.util.EntityCleaning;
import by.shakhau.dictionary.persistence.domain.FolderEntity;
import by.shakhau.dictionary.persistence.domain.TextFileEntity;
import by.shakhau.dictionary.persistence.domain.TextFileWordEntity;
import by.shakhau.dictionary.persistence.repository.FolderRepository;
import by.shakhau.dictionary.persistence.repository.TextFileRepository;
import by.shakhau.dictionary.persistence.repository.TextFileWordRepository;
import by.shakhau.dictionary.presentation.bean.FolderView;
import by.shakhau.dictionary.service.FolderService;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private TextFileRepository textFileRepository;

    @Autowired
    private TextFileWordRepository textFileWordRepository;

    @Override
    public List<FolderEntity> findAllFolders(Long userId) {
        return folderRepository.findByUserId(userId);
    }

    @Override
    public List<FolderEntity> rootFolders(Long userId) {
        List<FolderEntity> rootFolders = folderRepository.findByParentFolderIdIsNullAndUserId(userId);
        EntityCleaning.clearLazyFieldsFolder(rootFolders);
        return rootFolders;
    }

    @Override
    public FolderView folderChildren(Long folderId, Long userId) {
    	FolderView folderView = new FolderView();
        if (folderId == null) {
        	folderView.setFolders(rootFolders(userId));
            return folderView;
        }

        List<FolderEntity> folders = folderRepository.findByParentFolderIdAndUserId(folderId, userId);
        FolderEntity currentFolder = folders.isEmpty()
    			? folderRepository.findById(folderId).orElse(null)
				: folders.stream()
	        		.findFirst()
	        		.map(x -> x.getParentFolder())
	        		.orElse(null);
        
        if (currentFolder != null) {
            folderView.setCurrentFolderId(currentFolder.getId());
            if (currentFolder.getParentFolder() != null) {
                folderView.setParentFolderId(currentFolder.getParentFolder().getId());
            }
            EntityCleaning.clearLazyFields(currentFolder);
        }

        EntityCleaning.clearLazyFieldsFolder(folders);
        folderView.setFolders(folders);

        return folderView;
    }

    @Override
    public List<String> folderPath(Long folderId) {
        if (folderId == null) {
            return Collections.emptyList();
        }
        
        FolderEntity folder = folderRepository.findById(folderId).orElse(null);
        List<String> folderPath = new ArrayList<>();
        FolderEntity folderSearch = folder;
        while (folderSearch != null) {
            folderPath.add(folderSearch.getName());
            folderSearch = folderSearch.getParentFolder();
        }
        Collections.reverse(folderPath);
        return folderPath;
    }

    @Override
    public void addFolder(Long userId, Long parentFolderId, FolderEntity folder) {
        FolderEntity parentFolder = null;
        
        if (parentFolderId != null) {
            parentFolder = folderRepository.findById(parentFolderId).orElse(null);
        }

        folder.setParentFolder(parentFolder);
        folder.setUserId(userId);
        folderRepository.save(folder);
    }

    @Override
    public void renameFolder(Long folderId, String newFolderName) {
        FolderEntity folder = folderRepository.findById(folderId).orElse(null);
        folder.setName(newFolderName);
        folderRepository.save(folder);
    }

    @Override
    public void deleteFolder(Long folderId, Long userId) {
        FolderEntity folder = folderRepository.findById(folderId).orElse(null);
        deleteFolder(folder, userId);
    }

	@Override
	public List<Long> findAllChildrenIds(Long folderId, Long userId) {
		List<Long> childrenIdsResult = new ArrayList<>();
		findChildrenIds(folderId, userId, childrenIdsResult);
		return childrenIdsResult;
	}

	@Override
	public List<Long> findAllIds(Long folderId, Long userId) {
		if (folderId == null) {
			List<FolderEntity> folders = folderRepository.findByUserId(userId);
			return folders.stream()
					.map(f -> f.getId())
					.collect(Collectors.toList());
		}
		List<Long> ids = findAllChildrenIds(folderId, userId);
		ids.add(folderId);
		return ids;
	}

	private void deleteFolder(FolderEntity folder, Long userId) {
		Long folderId = folder.getId();
        List<FolderEntity> children = folderRepository.findByParentFolderIdAndUserId(folderId, userId);
        for (FolderEntity child : children) {
            deleteFolder(child, userId);
        }
        List<TextFileEntity> textFiles = textFileRepository.findByFolderIdAndUserId(folderId, folder.getUserId());
        List<TextFileWordEntity> textFileWords = textFileWordRepository.findByTextFileFolderId(folderId);
        textFileWordRepository.deleteAll(textFileWords);
        textFileRepository.deleteAll(textFiles);
        folderRepository.delete(folder);
    }

	private void findChildrenIds(Long folderId, Long userId, List<Long> childrenIdsResult) {
		List<Long> childrenIds = folderRepository.findByParentFolderIdAndUserId(folderId, userId).stream()
				.map(FolderEntity::getId)
				.collect(Collectors.toList());
		childrenIdsResult.addAll(childrenIds);
		for (Long childId : childrenIds) {
			findChildrenIds(childId, userId, childrenIdsResult);
		}
	}
}
