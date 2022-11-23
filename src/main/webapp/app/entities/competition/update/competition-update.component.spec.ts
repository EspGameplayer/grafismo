import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompetitionService } from '../service/competition.service';
import { ICompetition, Competition } from '../competition.model';
import { ISponsor } from 'app/entities/sponsor/sponsor.model';
import { SponsorService } from 'app/entities/sponsor/service/sponsor.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { CompetitionUpdateComponent } from './competition-update.component';

describe('Competition Management Update Component', () => {
  let comp: CompetitionUpdateComponent;
  let fixture: ComponentFixture<CompetitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let competitionService: CompetitionService;
  let sponsorService: SponsorService;
  let teamService: TeamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompetitionUpdateComponent],
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
      .overrideTemplate(CompetitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    competitionService = TestBed.inject(CompetitionService);
    sponsorService = TestBed.inject(SponsorService);
    teamService = TestBed.inject(TeamService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sponsor query and add missing value', () => {
      const competition: ICompetition = { id: 456 };
      const sponsor: ISponsor = { id: 18856 };
      competition.sponsor = sponsor;
      const motmSponsor: ISponsor = { id: 36185 };
      competition.motmSponsor = motmSponsor;

      const sponsorCollection: ISponsor[] = [{ id: 49219 }];
      jest.spyOn(sponsorService, 'query').mockReturnValue(of(new HttpResponse({ body: sponsorCollection })));
      const additionalSponsors = [sponsor, motmSponsor];
      const expectedCollection: ISponsor[] = [...additionalSponsors, ...sponsorCollection];
      jest.spyOn(sponsorService, 'addSponsorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      expect(sponsorService.query).toHaveBeenCalled();
      expect(sponsorService.addSponsorToCollectionIfMissing).toHaveBeenCalledWith(sponsorCollection, ...additionalSponsors);
      expect(comp.sponsorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Competition query and add missing value', () => {
      const competition: ICompetition = { id: 456 };
      const parent: ICompetition = { id: 40406 };
      competition.parent = parent;

      const competitionCollection: ICompetition[] = [{ id: 84425 }];
      jest.spyOn(competitionService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionCollection })));
      const additionalCompetitions = [parent];
      const expectedCollection: ICompetition[] = [...additionalCompetitions, ...competitionCollection];
      jest.spyOn(competitionService, 'addCompetitionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      expect(competitionService.query).toHaveBeenCalled();
      expect(competitionService.addCompetitionToCollectionIfMissing).toHaveBeenCalledWith(competitionCollection, ...additionalCompetitions);
      expect(comp.competitionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Team query and add missing value', () => {
      const competition: ICompetition = { id: 456 };
      const teams: ITeam[] = [{ id: 66796 }];
      competition.teams = teams;

      const teamCollection: ITeam[] = [{ id: 59177 }];
      jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
      const additionalTeams = [...teams];
      const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
      jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      expect(teamService.query).toHaveBeenCalled();
      expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
      expect(comp.teamsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const competition: ICompetition = { id: 456 };
      const sponsor: ISponsor = { id: 73224 };
      competition.sponsor = sponsor;
      const motmSponsor: ISponsor = { id: 21771 };
      competition.motmSponsor = motmSponsor;
      const parent: ICompetition = { id: 67539 };
      competition.parent = parent;
      const teams: ITeam = { id: 63136 };
      competition.teams = [teams];

      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(competition));
      expect(comp.sponsorsSharedCollection).toContain(sponsor);
      expect(comp.sponsorsSharedCollection).toContain(motmSponsor);
      expect(comp.competitionsSharedCollection).toContain(parent);
      expect(comp.teamsSharedCollection).toContain(teams);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Competition>>();
      const competition = { id: 123 };
      jest.spyOn(competitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(competitionService.update).toHaveBeenCalledWith(competition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Competition>>();
      const competition = new Competition();
      jest.spyOn(competitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competition }));
      saveSubject.complete();

      // THEN
      expect(competitionService.create).toHaveBeenCalledWith(competition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Competition>>();
      const competition = { id: 123 };
      jest.spyOn(competitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(competitionService.update).toHaveBeenCalledWith(competition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSponsorById', () => {
      it('Should return tracked Sponsor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSponsorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompetitionById', () => {
      it('Should return tracked Competition primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetitionById(0, entity);
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
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTeam', () => {
      it('Should return option if no Team is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTeam(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Team for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTeam(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Team is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTeam(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
