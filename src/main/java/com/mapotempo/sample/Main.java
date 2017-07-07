package com.mapotempo.sample;

import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.Device;
import com.mapotempo.fleet.model.accessor.DeviceAccess;
import com.mapotempo.fleet.model.accessor.MissionAccess;
import com.mapotempo.fleet.model.Mission;
import com.mapotempo.fleet.model.submodel.Location;

import java.util.List;
import java.util.Scanner;

class Main {

  public static void main(String[] args) {
      try {
          DatabaseHandler connexionHandler = new DatabaseHandler("static", "static", "http://localhost:4985/db", new JavaContext());
          // DatabaseHandler connexionHandler = new DatabaseHandler("toto", "132456", "http://tigr0.com.mapotempo.com:4984/db");

          MissionAccess missionAccess = new MissionAccess(connexionHandler);
          DeviceAccess deviceAccess = new DeviceAccess(connexionHandler);

          boolean status = true;
          while (status) {
              Scanner keyboard = new Scanner(System.in);
              String nextLine = keyboard.nextLine();
              if (nextLine.equals("")) {
                  continue;
              }
              Option option = Option.fromString(nextLine);

              switch (option) {

                  case EXIT:
                      status = false;
                      break;
                  case ADD_MISSION:
                      System.out.print("Enter mission name : ");
                      String mName = keyboard.nextLine();
                      //Device tmp = deviceAccess.get("ea2cef1e-e3d3-4894-b88f-698e636da833");
                      Mission mission = new Mission(mName, new Location(45., 2.), null);
                      missionAccess.commit(mission);
                      break;
                  case UPDATE_MISSION:
                      System.out.print("Enter mission id : ");
                      String mMissionIdUp = keyboard.nextLine();
                      Mission pMissionUp = missionAccess.get(mMissionIdUp);
                      System.out.println("- - - - - - - - - - Mission Before - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                      System.out.println(pMissionUp.toString());
                      System.out.print("Enter new mission name : ");
                      String mNewName = keyboard.nextLine();
                      pMissionUp.mName = mNewName;
                      System.out.print("Enter new mission lat : ");
                      String mNewLat = keyboard.nextLine();
                      pMissionUp.mLocation = new Location(0,0);
                      pMissionUp.mLocation.setLat(Float.parseFloat(mNewLat));
                      System.out.print("Enter new mission lon : ");
                      String mNewLon = keyboard.nextLine();
                      pMissionUp.mLocation.setLon(Float.parseFloat(mNewLon));
                      missionAccess.commit(pMissionUp);
                      //System.out.println("- - - - - - - - - - Mission After - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                      //Mission pMissionUpdated = missionAccess.get(mMissionIdUp);
                      //System.out.println(pMissionUpdated.toString());
                      break;
                  case MISSION:
                      System.out.print("Enter mission id : ");
                      String mMissionId = keyboard.nextLine();
                      Mission pMission = missionAccess.get(mMissionId);
                      System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                      System.out.println(pMission.toString());
                      break;
                  case MISSIONS:
                      List<Mission> missions = missionAccess.getAll();
                      System.out.println("missions number : " + missions.size());
                      for (Mission data : missions) {
                          System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                          System.out.println(data.toString());
                      }
                      break;
                  case DELETE_MISSIONS:
                      if(missionAccess.deleteAll())
                          System.out.println("Delete successfully");
                      else
                          System.out.println("Delete fail");
                      break;
                  case MISSIONS_NAME:
                      System.out.print("Enter mission name : ");
                      String mMissionName = keyboard.nextLine();
                      List<Mission> missions_name = missionAccess.getAllDataName(mMissionName);
                      System.out.println("missions number : " + missions_name.size());
                      for (Mission data : missions_name) {
                          System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                          System.out.println(data.toString());
                      }
                      break;
                  case ADD_DEVICE:
                      System.out.print("Enter device name : ");
                      String dName = keyboard.nextLine();
                      Device device = new Device(dName);
                      deviceAccess.commit(device);
                      break;
                  case DEVICE:
                      System.out.print("Enter mission id : ");
                      String mDeviceId = keyboard.nextLine();
                      Device mDevice = deviceAccess.get(mDeviceId);
                      System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                      System.out.println(mDevice.toString());
                      break;
                  case DEVICES:
                      List<Device> devices = deviceAccess.getAll();
                      System.out.println("missions number : " + devices.size());
                      for (Device data : devices) {
                          System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                          System.out.println(data.toString());
                      }
                      break;
                  case DELETE_DEVICES:
                      if(deviceAccess.deleteAll())
                          System.out.println("Delete successfully");
                      else
                          System.out.println("Delete fail");
                      break;
                  case ALL:
                      connexionHandler.printAllData();
                      break;
                  case CONNEXION:
                      System.out.print("Enter status [true, false] : ");
                      boolean connexion = Boolean.valueOf(keyboard.nextLine());
                      System.out.println(connexion);
                      if(connexion)
                          connexionHandler.goOnline();
                      else
                          connexionHandler.goOffline();
                      break;
                  default:
                  case UNKNOWN:
                      System.out.println("Unkown option :");
                  case HELP:
                      System.out.println(Option.help_message());
                      break;
              }
          }
      } catch (CoreException e) {
          e.printStackTrace();
      }
  }

  private enum Option {
      EXIT("exit", "Close the program"),
      ADD_MISSION("add_mission", "Add a mission"),
      MISSION("mission", "Display a mission"),
      UPDATE_MISSION("update", "Update specific mission"),
      MISSIONS("missions", "Display all missions"),
      DELETE_MISSIONS("delete_missions", "Delete all missions"),
      MISSIONS_NAME("missions_name", "Display all missions name"),
      ADD_DEVICE("add_device", "Add a device"),
      DEVICE("device", "Display a device"),
      DEVICES("devices", "Display all devices"),
      DELETE_DEVICES("delete_devices", "Delete all devices"),
      ALL("all", "Display all model"),
      CONNEXION("connexion", "connexion : true - false"),
      HELP("help", "Display this help message"),
      UNKNOWN("", "Wrong option");

      private String mNameType;
      private String mHelpMessage;

      Option(String mNameType, String helpMessage) {
          this.mNameType = mNameType;
          mHelpMessage = helpMessage;
      }

      @Override
      public String toString() {
            return mNameType;
        }

      static public Option fromString(String nameType) {
          if (nameType.equals(EXIT.toString()))
              return EXIT;
          else if (nameType.equals(MISSION.toString()))
              return MISSION;
          else if (nameType.equals(UPDATE_MISSION.toString()))
              return UPDATE_MISSION;
          else if (nameType.equals(MISSIONS.toString()))
              return MISSIONS;
          else if (nameType.equals(DELETE_MISSIONS.toString()))
              return DELETE_MISSIONS;
          else if (nameType.equals(MISSIONS_NAME.toString()))
              return MISSIONS_NAME;
          else if (nameType.equals(ADD_MISSION.toString()))
              return ADD_MISSION;
          else if (nameType.equals(DEVICE.toString()))
              return DEVICE;
          else if (nameType.equals(DEVICES.toString()))
              return DEVICES;
          else if (nameType.equals(DELETE_DEVICES.toString()))
              return DELETE_DEVICES;
          else if (nameType.equals(ADD_DEVICE.toString()))
              return ADD_DEVICE;
          else if (nameType.equals(CONNEXION.toString()))
              return CONNEXION;
          else if (nameType.equals(ALL.toString()))
              return ALL;
          else if (nameType.equals(HELP.toString()))
              return HELP;
          else
              return UNKNOWN;
      }

      static public String help_message()
      {
          String message = "MAPOTEMPO couchbase test program : ";

          for(Option option : Option.values()) {
              if (!option.equals(UNKNOWN)) {
                  message = message + "\n   " + option.mNameType + " : " + option.mHelpMessage;
              }
          }
          return message;
      }
  }
}