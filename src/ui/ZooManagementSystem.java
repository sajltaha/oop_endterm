package ui;

import Models.*;
import DAO.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZooManagementSystem extends JFrame {

    private ZooDAO zooDAO = new ZooDAO();
    private CageDAO cageDAO = new CageDAO();
    private AnimalDAO animalDAO = new AnimalDAO();

    private Zoo currentZoo;

    public ZooManagementSystem() {
        setTitle("Zoo Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createMenuBar();
        initUI();
    }

    private void initUI() {
        mainMenuUI();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu zooMenu = new JMenu("Zoo");
        JMenuItem createZooItem = new JMenuItem("Create Zoo");
        JMenuItem selectZooItem = new JMenuItem("Select Zoo");
        JMenuItem updateZooItem = new JMenuItem("Update Zoo");
        JMenuItem deleteZooItem = new JMenuItem("Delete Zoo");

        createZooItem.addActionListener(e -> createZooUI());
        selectZooItem.addActionListener(e -> selectZooUI());
        updateZooItem.addActionListener(e -> updateZooUI());
        deleteZooItem.addActionListener(e -> deleteZooUI());

        zooMenu.add(createZooItem);
        zooMenu.add(selectZooItem);
        zooMenu.add(updateZooItem);
        zooMenu.add(deleteZooItem);

        JMenu cageMenu = new JMenu("Cage");
        JMenuItem createCageItem = new JMenuItem("Create Cage");
        JMenuItem updateCageItem = new JMenuItem("Update Cage");
        JMenuItem deleteCageItem = new JMenuItem("Delete Cage");

        createCageItem.addActionListener(e -> createCageUI());
        updateCageItem.addActionListener(e -> updateCageUI());
        deleteCageItem.addActionListener(e -> deleteCageUI());

        cageMenu.add(createCageItem);
        cageMenu.add(updateCageItem);
        cageMenu.add(deleteCageItem);

        JMenu animalMenu = new JMenu("Animal");
        JMenuItem addAnimalItem = new JMenuItem("Add Animal");
        JMenuItem updateAnimalItem = new JMenuItem("Update Animal");
        JMenuItem deleteAnimalItem = new JMenuItem("Delete Animal");
        JMenuItem transferAnimalItem = new JMenuItem("Transfer Animal");

        addAnimalItem.addActionListener(e -> addAnimalUI());
        updateAnimalItem.addActionListener(e -> updateAnimalUI());
        deleteAnimalItem.addActionListener(e -> deleteAnimalUI());
        transferAnimalItem.addActionListener(e -> transferAnimalUI());

        animalMenu.add(addAnimalItem);
        animalMenu.add(updateAnimalItem);
        animalMenu.add(deleteAnimalItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem displayAnimalsItem = new JMenuItem("Display Animals");
        JMenuItem searchAnimalsItem = new JMenuItem("Search Animals");

        displayAnimalsItem.addActionListener(e -> displayAnimalsUI());
        searchAnimalsItem.addActionListener(e -> searchAnimalsUI());

        viewMenu.add(displayAnimalsItem);
        viewMenu.add(searchAnimalsItem);

        menuBar.add(zooMenu);
        menuBar.add(cageMenu);
        menuBar.add(animalMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);
    }

    private void mainMenuUI() {
        getContentPane().removeAll();

        JLabel welcomeLabel = new JLabel("Welcome to the Zoo Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton createZooButton = new JButton("Create Zoo");
        JButton selectZooButton = new JButton("Select Zoo");

        createZooButton.addActionListener(e -> createZooUI());
        selectZooButton.addActionListener(e -> selectZooUI());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(createZooButton);
        panel.add(selectZooButton);

        getContentPane().add(welcomeLabel, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void createZooUI() {
        getContentPane().removeAll();

        JLabel label = new JLabel("Enter Zoo Name:");
        JTextField zooNameField = new JTextField(20);
        JButton createButton = new JButton("Create Zoo");

        createButton.addActionListener(e -> {
            String zooName = zooNameField.getText().trim();
            if (zooName.isEmpty()) {
                showErrorDialog("Zoo name cannot be empty.");
                return;
            }

            try {
                Zoo zoo = new Zoo(zooName);
                zooDAO.addZoo(zoo);
                currentZoo = zoo;
                JOptionPane.showMessageDialog(this, "Zoo created successfully!");
                mainMenuUI();
            } catch (SQLException ex) {
                showErrorDialog(ex.getMessage());
            }
        });

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(zooNameField);
        panel.add(createButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void selectZooUI() {
        getContentPane().removeAll();

        try {
            List<Zoo> zoos = zooDAO.getAllZoos();
            if (zoos.isEmpty()) {
                showErrorDialog("No zoos available. Please create a zoo first.");
                mainMenuUI();
                return;
            }

            JLabel zooLabel = new JLabel("Select a Zoo:");
            JComboBox<Zoo> zooComboBox = new JComboBox<>(zoos.toArray(new Zoo[0]));
            JButton selectButton = new JButton("Select");

            selectButton.addActionListener(e -> {
                currentZoo = (Zoo) zooComboBox.getSelectedItem();
                JOptionPane.showMessageDialog(this, "Zoo selected: " + currentZoo.getName());
                mainMenuUI();
            });

            JPanel panel = new JPanel();
            panel.add(zooLabel);
            panel.add(zooComboBox);
            panel.add(selectButton);

            getContentPane().add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SQLException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void updateZooUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        JLabel nameLabel = new JLabel("Zoo Name:");
        JTextField nameField = new JTextField(currentZoo.getName(), 20);
        JButton updateButton = new JButton("Update Zoo");

        updateButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showErrorDialog("Zoo name cannot be empty.");
                return;
            }

            try {
                currentZoo.setName(name);
                zooDAO.updateZoo(currentZoo);
                JOptionPane.showMessageDialog(this, "Zoo updated successfully!");
                mainMenuUI();
            } catch (SQLException ex) {
                showErrorDialog(ex.getMessage());
            }
        });

        JPanel panel = new JPanel();
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(updateButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void deleteZooUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the current zoo?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                zooDAO.deleteZoo(currentZoo.getId());
                currentZoo = null;

                JOptionPane.showMessageDialog(this, "Zoo deleted successfully!");
                mainMenuUI();
            } catch (SQLException ex) {
                showErrorDialog(ex.getMessage());
            }
        } else {
            mainMenuUI();
        }
    }

    private void createCageUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        JLabel numberLabel = new JLabel("Cage Number:");
        JTextField numberField = new JTextField(5);

        JLabel sizeLabel = new JLabel("Cage Size:");
        JTextField sizeField = new JTextField(5);

        JLabel maxAnimalsLabel = new JLabel("Max Number of Animals:");
        JTextField maxAnimalsField = new JTextField(5);

        JButton createButton = new JButton("Create Cage");

        createButton.addActionListener(e -> {
            String numberText = numberField.getText().trim();
            String sizeText = sizeField.getText().trim();
            String maxAnimalsText = maxAnimalsField.getText().trim();

            if (numberText.isEmpty() || sizeText.isEmpty() || maxAnimalsText.isEmpty()) {
                showErrorDialog("All fields must be filled.");
                return;
            }

            try {
                int number = Integer.parseInt(numberText);
                int size = Integer.parseInt(sizeText);
                int maxAnimals = Integer.parseInt(maxAnimalsText);

                Cage cage = new Cage(currentZoo.getId(), number, size, maxAnimals);
                cageDAO.addCage(cage);
                JOptionPane.showMessageDialog(this, "Cage created successfully!");
                mainMenuUI();
            } catch (NumberFormatException nfe) {
                showErrorDialog("Please enter valid numerical values.");
            } catch (SQLException ex) {
                showErrorDialog(ex.getMessage());
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(numberLabel);
        panel.add(numberField);
        panel.add(sizeLabel);
        panel.add(sizeField);
        panel.add(maxAnimalsLabel);
        panel.add(maxAnimalsField);
        panel.add(new JLabel());
        panel.add(createButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void updateCageUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        try {
            List<Cage> cages = cageDAO.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                showErrorDialog("No cages available to update.");
                mainMenuUI();
                return;
            }

            JLabel selectCageLabel = new JLabel("Select Cage:");
            JComboBox<Cage> cageComboBox = new JComboBox<>(cages.toArray(new Cage[0]));

            JLabel numberLabel = new JLabel("Cage Number:");
            JTextField numberField = new JTextField(5);

            JLabel sizeLabel = new JLabel("Cage Size:");
            JTextField sizeField = new JTextField(5);

            JLabel maxAnimalsLabel = new JLabel("Max Animals:");
            JTextField maxAnimalsField = new JTextField(5);

            JButton updateButton = new JButton("Update Cage");

            cageComboBox.addActionListener(e -> {
                Cage selectedCage = (Cage) cageComboBox.getSelectedItem();
                numberField.setText(String.valueOf(selectedCage.getNumber()));
                sizeField.setText(String.valueOf(selectedCage.getSize()));
                maxAnimalsField.setText(String.valueOf(selectedCage.getMaxAnimals()));
            });

            updateButton.addActionListener(e -> {
                Cage selectedCage = (Cage) cageComboBox.getSelectedItem();

                String numberText = numberField.getText().trim();
                String sizeText = sizeField.getText().trim();
                String maxAnimalsText = maxAnimalsField.getText().trim();

                if (numberText.isEmpty() || sizeText.isEmpty() || maxAnimalsText.isEmpty()) {
                    showErrorDialog("All fields must be filled.");
                    return;
                }

                try {
                    int number = Integer.parseInt(numberText);
                    int size = Integer.parseInt(sizeText);
                    int maxAnimals = Integer.parseInt(maxAnimalsText);

                    selectedCage.setNumber(number);
                    selectedCage.setSize(size);
                    selectedCage.setMaxAnimals(maxAnimals);

                    cageDAO.updateCage(selectedCage);
                    JOptionPane.showMessageDialog(this, "Cage updated successfully!");
                    mainMenuUI();
                } catch (NumberFormatException nfe) {
                    showErrorDialog("Please enter valid numerical values.");
                } catch (SQLException ex) {
                    showErrorDialog(ex.getMessage());
                }
            });

            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
            panel.add(selectCageLabel);
            panel.add(cageComboBox);
            panel.add(numberLabel);
            panel.add(numberField);
            panel.add(sizeLabel);
            panel.add(sizeField);
            panel.add(maxAnimalsLabel);
            panel.add(maxAnimalsField);
            panel.add(new JLabel());
            panel.add(updateButton);

            getContentPane().add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SQLException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void deleteCageUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        try {
            List<Cage> cages = cageDAO.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                showErrorDialog("No cages available to delete.");
                mainMenuUI();
                return;
            }

            JLabel selectCageLabel = new JLabel("Select Cage:");
            JComboBox<Cage> cageComboBox = new JComboBox<>(cages.toArray(new Cage[0]));
            JButton deleteButton = new JButton("Delete Cage");

            deleteButton.addActionListener(e -> {
                Cage selectedCage = (Cage) cageComboBox.getSelectedItem();

                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected cage?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        cageDAO.deleteCage(selectedCage.getId());
                        JOptionPane.showMessageDialog(this, "Cage deleted successfully!");
                        mainMenuUI();
                    } catch (SQLException ex) {
                        showErrorDialog(ex.getMessage());
                    }
                } else {
                    mainMenuUI();
                }
            });

            JPanel panel = new JPanel();
            panel.add(selectCageLabel);
            panel.add(cageComboBox);
            panel.add(deleteButton);

            getContentPane().add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();

        } catch (SQLException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void addAnimalUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        try {
            List<Cage> cages = cageDAO.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                showErrorDialog("No cages available. Please create a cage first.");
                mainMenuUI();
                return;
            }

            JLabel nameLabel = new JLabel("Animal Name:");
            JTextField nameField = new JTextField(10);

            JLabel predatorLabel = new JLabel("Is Predator:");
            JCheckBox predatorCheckBox = new JCheckBox();

            JLabel cageLabel = new JLabel("Select Cage:");
            JComboBox<Cage> cageComboBox = new JComboBox<>(cages.toArray(new Cage[0]));

            JButton addButton = new JButton("Add Animal");

            addButton.addActionListener(e -> {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    showErrorDialog("Animal name cannot be empty.");
                    return;
                }

                Cage selectedCage = (Cage) cageComboBox.getSelectedItem();
                boolean isPredator = predatorCheckBox.isSelected();

                try {
                    if (cageDAO.isCageAtMaxCapacity(selectedCage.getId())) {
                        showErrorDialog("Selected cage is at maximum capacity.");
                        return;
                    }

                    Animal animal = new Animal(selectedCage.getId(), name, isPredator);
                    animalDAO.addAnimal(animal);
                    cageDAO.incrementCurrentAnimals(selectedCage.getId());
                    JOptionPane.showMessageDialog(this, "Animal added successfully!");
                    mainMenuUI();
                } catch (SQLException ex) {
                    showErrorDialog(ex.getMessage());
                }
            });

            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(predatorLabel);
            panel.add(predatorCheckBox);
            panel.add(cageLabel);
            panel.add(cageComboBox);
            panel.add(new JLabel());
            panel.add(addButton);

            getContentPane().add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SQLException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void updateAnimalUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        try {
            List<Cage> cages = cageDAO.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                showErrorDialog("No cages available.");
                mainMenuUI();
                return;
            }

            List<Animal> animals = new ArrayList<>();
            for (Cage cage : cages) {
                animals.addAll(animalDAO.getAnimalsByCageId(cage.getId()));
            }

            if (animals.isEmpty()) {
                showErrorDialog("No animals available to update.");
                mainMenuUI();
                return;
            }

            JLabel selectAnimalLabel = new JLabel("Select Animal:");
            JComboBox<Animal> animalComboBox = new JComboBox<>(animals.toArray(new Animal[0]));

            JLabel nameLabel = new JLabel("Animal Name:");
            JTextField nameField = new JTextField(10);

            JLabel predatorLabel = new JLabel("Is Predator:");
            JCheckBox predatorCheckBox = new JCheckBox();

            JLabel cageLabel = new JLabel("Select Cage:");
            JComboBox<Cage> cageComboBox = new JComboBox<>(cages.toArray(new Cage[0]));

            JButton updateButton = new JButton("Update Animal");

            animalComboBox.addActionListener(e -> {
                Animal selectedAnimal = (Animal) animalComboBox.getSelectedItem();
                nameField.setText(selectedAnimal.getName());
                predatorCheckBox.setSelected(selectedAnimal.isPredator());
                Cage animalCage = cages.stream().filter(c -> c.getId() == selectedAnimal.getCageId()).findFirst()
                        .orElse(null);
                cageComboBox.setSelectedItem(animalCage);
            });

            JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
            panel.add(selectAnimalLabel);
            panel.add(animalComboBox);
            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(predatorLabel);
            panel.add(predatorCheckBox);
            panel.add(cageLabel);
            panel.add(cageComboBox);
            panel.add(new JLabel());
            panel.add(updateButton);

            getContentPane().add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();

        } catch (SQLException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void deleteAnimalUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        try {
            List<Cage> cages = cageDAO.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                showErrorDialog("No cages available.");
                mainMenuUI();
                return;
            }

            List<Animal> animals = new ArrayList<>();
            for (Cage cage : cages) {
                animals.addAll(animalDAO.getAnimalsByCageId(cage.getId()));
            }

            if (animals.isEmpty()) {
                showErrorDialog("No animals available to delete.");
                mainMenuUI();
                return;
            }

            JLabel selectAnimalLabel = new JLabel("Select Animal:");
            JComboBox<Animal> animalComboBox = new JComboBox<>(animals.toArray(new Animal[0]));
            JButton deleteButton = new JButton("Delete Animal");

            deleteButton.addActionListener(e -> {
                Animal selectedAnimal = (Animal) animalComboBox.getSelectedItem();

                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected animal?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        animalDAO.deleteAnimal(selectedAnimal.getId());
                        cageDAO.decrementCurrentAnimals(selectedAnimal.getCageId());
                        JOptionPane.showMessageDialog(this, "Animal deleted successfully!");
                        mainMenuUI();
                    } catch (SQLException ex) {
                        showErrorDialog(ex.getMessage());
                    }
                } else {
                    mainMenuUI();
                }
            });

            JPanel panel = new JPanel();
            panel.add(selectAnimalLabel);
            panel.add(animalComboBox);
            panel.add(deleteButton);

            getContentPane().add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();

        } catch (SQLException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void displayAnimalsUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        try {
            List<Cage> cages = cageDAO.getCagesByZooId(currentZoo.getId());
            if (cages.isEmpty()) {
                showErrorDialog("No cages available to display.");
                mainMenuUI();
                return;
            }

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (Cage cage : cages) {
                JLabel cageLabel = new JLabel(cage.toString());
                cageLabel.setFont(new Font("Arial", Font.BOLD, 14));
                panel.add(cageLabel);

                List<Animal> animals = animalDAO.getAnimalsByCageId(cage.getId());
                if (animals.isEmpty()) {
                    panel.add(new JLabel("   No animals in this cage."));
                } else {
                    for (Animal animal : animals) {
                        panel.add(new JLabel("   " + animal.toString()));
                    }
                }
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            JButton backButton = new JButton("Back to Main Menu");
            backButton.addActionListener(e -> mainMenuUI());

            getContentPane().add(scrollPane, BorderLayout.CENTER);
            getContentPane().add(backButton, BorderLayout.SOUTH);

            revalidate();
            repaint();

        } catch (SQLException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void searchAnimalsUI() {
        getContentPane().removeAll();

        if (currentZoo == null) {
            showErrorDialog("No zoo selected. Please select a zoo first.");
            mainMenuUI();
            return;
        }

        JLabel searchLabel = new JLabel("Enter Animal Name:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        searchButton.addActionListener(e -> {
            resultPanel.removeAll();
            String searchName = searchField.getText().trim();
            if (searchName.isEmpty()) {
                showErrorDialog("Please enter an animal name to search.");
                return;
            }

            try {
                List<Animal> matchingAnimals = animalDAO.searchAnimalsByName(searchName, currentZoo.getId());
                if (matchingAnimals.isEmpty()) {
                    resultPanel.add(new JLabel("No animals found matching the name."));
                } else {
                    for (Animal animal : matchingAnimals) {
                        Cage cage = cageDAO.getCageById(animal.getCageId());
                        JLabel animalLabel = new JLabel(animal.toString() + " in " + cage.toString());
                        resultPanel.add(animalLabel);
                    }
                }
                revalidate();
                repaint();
            } catch (SQLException ex) {
                showErrorDialog(ex.getMessage());
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultPanel);

        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> mainMenuUI());
        getContentPane().add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void transferAnimalUI() {
    }
}