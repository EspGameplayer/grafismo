import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITeamStaffMember, TeamStaffMember } from '../team-staff-member.model';
import { TeamStaffMemberService } from '../service/team-staff-member.service';

import { TeamStaffMemberRoutingResolveService } from './team-staff-member-routing-resolve.service';

describe('TeamStaffMember routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TeamStaffMemberRoutingResolveService;
  let service: TeamStaffMemberService;
  let resultTeamStaffMember: ITeamStaffMember | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TeamStaffMemberRoutingResolveService);
    service = TestBed.inject(TeamStaffMemberService);
    resultTeamStaffMember = undefined;
  });

  describe('resolve', () => {
    it('should return ITeamStaffMember returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTeamStaffMember = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTeamStaffMember).toEqual({ id: 123 });
    });

    it('should return new ITeamStaffMember if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTeamStaffMember = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTeamStaffMember).toEqual(new TeamStaffMember());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TeamStaffMember })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTeamStaffMember = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTeamStaffMember).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
