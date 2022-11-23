import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlayerService } from '../service/player.service';
import { IPlayer, Player } from '../player.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';

import { PlayerUpdateComponent } from './player-update.component';

describe('Player Management Update Component', () => {
  let comp: PlayerUpdateComponent;
  let fixture: ComponentFixture<PlayerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playerService: PlayerService;
  let personService: PersonService;
  let teamService: TeamService;
  let positionService: PositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlayerUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PlayerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playerService = TestBed.inject(PlayerService);
    personService = TestBed.inject(PersonService);
    teamService = TestBed.inject(TeamService);
    positionService = TestBed.inject(PositionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call person query and add missing value', () => {
      const player: IPlayer = { id: 456 };
      const person: IPerson = { id: 6805 };
      player.person = person;

      const personCollection: IPerson[] = [{ id: 22506 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const expectedCollection: IPerson[] = [person, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ player });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
      expect(comp.peopleCollection).toEqual(expectedCollection);
    });

    it('Should call Team query and add missing value', () => {
      const player: IPlayer = { id: 456 };
      const team: ITeam = { id: 84065 };
      player.team = team;

      const teamCollection: ITeam[] = [{ id: 81969 }];
      jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
      const additionalTeams = [team];
      const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
      jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ player });
      comp.ngOnInit();

      expect(teamService.query).toHaveBeenCalled();
      expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
      expect(comp.teamsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Position query and add missing value', () => {
      const player: IPlayer = { id: 456 };
      const positions: IPosition[] = [{ id: 66688 }];
      player.positions = positions;

      const positionCollection: IPosition[] = [{ id: 46425 }];
      jest.spyOn(positionService, 'query').mockReturnValue(of(new HttpResponse({ body: positionCollection })));
      const additionalPositions = [...positions];
      const expectedCollection: IPosition[] = [...additionalPositions, ...positionCollection];
      jest.spyOn(positionService, 'addPositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ player });
      comp.ngOnInit();

      expect(positionService.query).toHaveBeenCalled();
      expect(positionService.addPositionToCollectionIfMissing).toHaveBeenCalledWith(positionCollection, ...additionalPositions);
      expect(comp.positionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const player: IPlayer = { id: 456 };
      const person: IPerson = { id: 9216 };
      player.person = person;
      const team: ITeam = { id: 68928 };
      player.team = team;
      const positions: IPosition = { id: 49918 };
      player.positions = [positions];

      activatedRoute.data = of({ player });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(player));
      expect(comp.peopleCollection).toContain(person);
      expect(comp.teamsSharedCollection).toContain(team);
      expect(comp.positionsSharedCollection).toContain(positions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Player>>();
      const player = { id: 123 };
      jest.spyOn(playerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ player });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: player }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(playerService.update).toHaveBeenCalledWith(player);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Player>>();
      const player = new Player();
      jest.spyOn(playerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ player });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: player }));
      saveSubject.complete();

      // THEN
      expect(playerService.create).toHaveBeenCalledWith(player);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Player>>();
      const player = { id: 123 };
      jest.spyOn(playerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ player });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playerService.update).toHaveBeenCalledWith(player);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonById', () => {
      it('Should return tracked Person primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTeamById', () => {
      it('Should return tracked Team primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTeamById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPositionById', () => {
      it('Should return tracked Position primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPositionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPosition', () => {
      it('Should return option if no Position is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPosition(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Position for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPosition(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Position is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPosition(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
