import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SponsorService } from '../service/sponsor.service';
import { ISponsor, Sponsor } from '../sponsor.model';

import { SponsorUpdateComponent } from './sponsor-update.component';

describe('Sponsor Management Update Component', () => {
  let comp: SponsorUpdateComponent;
  let fixture: ComponentFixture<SponsorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sponsorService: SponsorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SponsorUpdateComponent],
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
      .overrideTemplate(SponsorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SponsorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sponsorService = TestBed.inject(SponsorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sponsor: ISponsor = { id: 456 };

      activatedRoute.data = of({ sponsor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sponsor));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sponsor>>();
      const sponsor = { id: 123 };
      jest.spyOn(sponsorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sponsor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sponsor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sponsorService.update).toHaveBeenCalledWith(sponsor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sponsor>>();
      const sponsor = new Sponsor();
      jest.spyOn(sponsorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sponsor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sponsor }));
      saveSubject.complete();

      // THEN
      expect(sponsorService.create).toHaveBeenCalledWith(sponsor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sponsor>>();
      const sponsor = { id: 123 };
      jest.spyOn(sponsorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sponsor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sponsorService.update).toHaveBeenCalledWith(sponsor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
